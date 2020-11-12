package com.akaene.flagship.persistence.dao;

import com.akaene.flagship.exception.PersistenceException;
import com.akaene.flagship.model.UserAccount;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAccountDao extends BaseDao<UserAccount> {

    @Autowired
    public UserAccountDao(EntityManager em) {
        super(UserAccount.class, em);
    }

    @Override
    public List<UserAccount> findAll() {
        try {
            return em.createQuery("SELECT u FROM UserAccount u ORDER BY u.username", UserAccount.class).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    public UserAccount findByUsername(String username) {
        try {
            return em.createQuery(
                    "SELECT u FROM UserAccount u WHERE u.username=:username", UserAccount.class)
                     .setParameter("username", username)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
