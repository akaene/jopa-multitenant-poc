package com.akaene.flagship.service.repository;

import com.akaene.flagship.model.UserAccount;
import com.akaene.flagship.persistence.dao.UserAccountDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserAccountRepositoryService {

    private final UserAccountDao dao;

    public UserAccountRepositoryService(UserAccountDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void persist(UserAccount account) {
        Objects.requireNonNull(account);
        dao.persist(account);
    }

    public List<UserAccount> findAll() {
        return dao.findAll();
    }

    public UserAccount findByUsername(String username) {
        return dao.findByUsername(username);
    }
}
