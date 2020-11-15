package com.akaene.flagship.security;

import com.akaene.flagship.dto.UserAccountDto;
import com.akaene.flagship.exception.security.IncompleteJwtException;
import com.akaene.flagship.exception.security.TokenExpiredException;
import com.akaene.flagship.security.model.FlagshipUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.JacksonDeserializer;
import io.jsonwebtoken.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JwtUtils.class);

    static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private final ObjectMapper objectMapper;

    private final Key key;

    @Autowired
    public JwtUtils(ObjectMapper objectMapper, @Value("${jwt.secretKey}") String jwtSecretKey) {
        this.objectMapper = objectMapper;
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOG.warn("Unable to determine hostname. JWT key won't contain it.");
            hostName = "";
        }
        this.key = Keys.hmacShaKeyFor((jwtSecretKey + hostName).getBytes());
    }

    /**
     * Generates a JSON Web Token for the specified authenticated user.
     *
     * @param userDetails User info
     * @return Generated JWT has
     */
    public String generateToken(FlagshipUserDetails userDetails) {
        final Date issued = new Date();
        return Jwts.builder().setSubject(userDetails.getUsername())
                   .setId(userDetails.getUserId().toString())
                   .setIssuedAt(issued)
                   .setExpiration(new Date(issued.getTime() + SecurityConstants.SESSION_TIMEOUT))
                   .claim(SecurityConstants.JWT_ROLE_CLAIM, mapAuthoritiesToClaim(userDetails.getAuthorities()))
                   .claim(SecurityConstants.TENANT_CLAIM, userDetails.getTenant())
                   .signWith(key, SIGNATURE_ALGORITHM)
                   .serializeToJsonWith(new JacksonSerializer<>(objectMapper))
                   .compact();
    }

    private static String mapAuthoritiesToClaim(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority)
                          .collect(Collectors.joining(SecurityConstants.JWT_ROLE_DELIMITER));
    }

    /**
     * Retrieves user info from the specified JWT.
     * <p>
     * The token is first validated for correct format and expiration date.
     *
     * @param token JWT to read
     * @return User info retrieved from the specified token
     */
    public FlagshipUserDetails extractUserInfo(String token) {
        Objects.requireNonNull(token);
        try {
            final Claims claims = getClaimsFromToken(token);
            verifyAttributePresence(claims);
            final UserAccountDto user = new UserAccountDto();
            user.setUri(URI.create(claims.getId()));
            user.setUsername(claims.getSubject());
            final String roles = claims.get(SecurityConstants.JWT_ROLE_CLAIM, String.class);
            final FlagshipUserDetails userDetails = new FlagshipUserDetails(user, mapClaimToAuthorities(roles));
            userDetails.setTenant(URI.create(claims.get(SecurityConstants.TENANT_CLAIM, String.class)));
            return userDetails;
        } catch (IllegalArgumentException e) {
            throw new JwtException("Unable to parse user identifier from the specified JWT.", e);
        }
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(key)
                       .deserializeJsonWith(new JacksonDeserializer<>(objectMapper))
                       .parseClaimsJws(token).getBody();
        } catch (MalformedJwtException e) {
            throw new JwtException("Unable to parse the specified JWT.", e);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(e.getMessage());
        } catch (SignatureException | WeakKeyException e) {
            throw new JwtException("JWT has invalid signature.", e);
        }
    }

    private static void verifyAttributePresence(Claims claims) {
        if (claims.getSubject() == null) {
            throw new IncompleteJwtException("JWT is missing subject.");
        }
        if (claims.getId() == null) {
            throw new IncompleteJwtException("JWT is missing id.");
        }
        if (claims.getExpiration() == null) {
            throw new TokenExpiredException("Missing token expiration info. Assuming expired.");
        }
        if (claims.get(SecurityConstants.TENANT_CLAIM) == null) {
            throw new IncompleteJwtException("JWT is missing tenant information.");
        }
    }

    private static List<GrantedAuthority> mapClaimToAuthorities(String claim) {
        if (claim == null) {
            return Collections.emptyList();
        }
        final String[] roles = claim.split(SecurityConstants.JWT_ROLE_DELIMITER);
        final List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    /**
     * Updates issuing and expiration date of the specified token, generating a new one.
     *
     * @param token The token to refresh
     * @return Newly generated token with updated expiration date
     */
    public String refreshToken(String token) {
        Objects.requireNonNull(token);
        final Claims claims = getClaimsFromToken(token);
        final Date issuedAt = new Date();
        claims.setIssuedAt(issuedAt);
        claims.setExpiration(new Date(issuedAt.getTime() + SecurityConstants.SESSION_TIMEOUT));
        return Jwts.builder().setClaims(claims)
                   .signWith(key, SIGNATURE_ALGORITHM)
                   .serializeToJsonWith(new JacksonSerializer<>(objectMapper))
                   .compact();
    }
}
