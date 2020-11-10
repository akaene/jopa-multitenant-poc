package com.akaene.flagship.persistence.dao;

import com.akaene.flagship.exception.PersistenceException;
import com.akaene.flagship.model.util.EntityToOwlClassMapper;
import com.akaene.flagship.model.util.HasUri;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Base implementation of the generic DAO.
 */
public abstract class BaseDao<T extends HasUri> implements GenericDao<T> {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);

    protected final EntityManager em;

    protected final Class<T> type;
    final URI typeUri;

    protected BaseDao(Class<T> type, EntityManager em) {
        this.type = type;
        this.typeUri = URI.create(EntityToOwlClassMapper.getOwlClassForEntity(type));
        this.em = em;
    }

    @Override
    public T find(URI uri) {
        Objects.requireNonNull(uri);
        try {
            return em.find(type, uri);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return em.createNativeQuery("SELECT e FROM " + type.getSimpleName() + " e", type).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void persist(T entity) {
        Objects.requireNonNull(entity);
        try {
            em.persist(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void persist(Collection<T> entities) {
        Objects.requireNonNull(entities);
        if (entities.isEmpty()) {
            return;
        }
        try {
            entities.forEach(this::persist);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public T update(T entity) {
        Objects.requireNonNull(entity);
        try {
            return em.merge(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(T entity) {
        Objects.requireNonNull(entity);
        try {
            final T toRemove = em.merge(entity);
            assert toRemove != null;
            em.remove(toRemove);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void remove(Collection<T> entities) {
        Objects.requireNonNull(entities);
        if (entities.isEmpty()) {
            return;
        }
        try {
            entities.forEach(entity -> {
                final T toRemove = em.merge(entity);
                assert toRemove != null;
                em.remove(toRemove);
            });
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public boolean exists(URI uri) {
        if (uri == null) {
            return false;
        }
        try {
            final String owlClass = type.getDeclaredAnnotation(OWLClass.class).iri();
            return em.createNativeQuery("ASK { ?individual a ?type . }", Boolean.class).setParameter("individual", uri)
                     .setParameter("type", URI.create(owlClass)).getSingleResult();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
