package com.akaene.flagship.service;

import com.akaene.flagship.model.UserAccount;
import com.akaene.flagship.persistence.dao.UserAccountDao;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserAccountService {

    private final UserAccountDao dao;

    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountDao dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void create(UserAccount account) {
        Objects.requireNonNull(account);
        account.encodePassword(passwordEncoder);
        dao.persist(account);
    }

    public List<UserAccount> findAll() {
        return dao.findAll();
    }

    public UserAccount findByUsername(String username) {
        return dao.findByUsername(username);
    }
}
