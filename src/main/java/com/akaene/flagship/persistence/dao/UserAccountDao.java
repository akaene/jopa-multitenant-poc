package com.akaene.flagship.persistence.dao;

import com.akaene.flagship.exception.PersistenceException;
import com.akaene.flagship.model.UserAccount;
import com.akaene.flagship.util.Constants;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.reporting.model.Vocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;

@Repository
public class UserAccountDao extends BaseDao<UserAccount> {

    @Autowired
    public UserAccountDao(EntityManager em) {
        super(UserAccount.class, em);
    }

    public UserAccount findByUsername(String username) {
        try {
            return em.createNativeQuery(
                    "SELECT ?x WHERE { ?x ?hasUsername ?username . }", UserAccount.class)
                     .setParameter("hasUsername", URI.create(Vocabulary.s_p_accountName))
                     .setParameter("username", username, Constants.PU_LANGUAGE)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
