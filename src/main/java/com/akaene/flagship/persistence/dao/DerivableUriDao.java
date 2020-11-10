package com.akaene.flagship.persistence.dao;

import com.akaene.flagship.model.util.HasDerivableUri;
import cz.cvut.kbss.jopa.model.EntityManager;

import java.util.Objects;

/**
 * Data access object for classes with derivable URI.
 * <p>
 * Makes sure that the URI is generated before the instance is persisted.
 *
 * @param <T> Entity type managed by this DAO
 */
abstract class DerivableUriDao<T extends HasDerivableUri> extends BaseDao<T> {

    protected DerivableUriDao(Class<T> type, EntityManager em) {
        super(type, em);
    }

    /**
     * Generates URI and then calls persist.
     *
     * @param entity Entity to persist
     */
    @Override
    public void persist(T entity) {
        Objects.requireNonNull(entity);
        entity.generateUri();
        super.persist(entity);
    }
}
