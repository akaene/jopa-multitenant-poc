package com.akaene.flagship.service.security;

import com.akaene.flagship.dto.mapper.UserAccountMapper;
import com.akaene.flagship.model.UserAccount;
import com.akaene.flagship.persistence.dao.UserAccountDao;
import com.akaene.flagship.security.model.FlagshipUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserAccountDao userAccountDao;

    private final UserAccountMapper accountMapper;

    @Autowired
    public UserDetailsService(UserAccountDao userAccountDao, UserAccountMapper accountMapper) {
        this.userAccountDao = userAccountDao;
        this.accountMapper = accountMapper;
    }

    @Override
    public FlagshipUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserAccount account = userAccountDao.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("User with username " + username + " not found.");
        }
        return new FlagshipUserDetails(accountMapper.userAccountToDto(account));
    }
}
