package com.akaene.flagship.util;

import com.akaene.flagship.exception.security.TenantNotFoundException;
import com.akaene.flagship.security.SecurityConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Component
public class HttpUtils {

    private final HttpServletRequest request;

    public HttpUtils(HttpServletRequest request) {
        this.request = request;
    }

    public URI resolveTenant() {
        final String tenantIri = request.getHeader(SecurityConstants.TENANT_HEADER);
        if (tenantIri == null || tenantIri.trim().isEmpty()) {
            throw new TenantNotFoundException();
        }
        return URI.create(tenantIri);
    }
}
