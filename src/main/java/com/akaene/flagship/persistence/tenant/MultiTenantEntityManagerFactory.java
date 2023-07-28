package com.akaene.flagship.persistence.tenant;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.PersistenceUnitUtil;
import cz.cvut.kbss.jopa.model.metamodel.Metamodel;
import cz.cvut.kbss.jopa.model.query.Query;
import cz.cvut.kbss.jopa.sessions.Cache;

import java.util.Map;

public class MultiTenantEntityManagerFactory implements EntityManagerFactory {

    private final TenantPersistenceProvider tenantPersistenceProvider;

    public MultiTenantEntityManagerFactory(TenantPersistenceProvider tenantPersistenceProvider) {
        this.tenantPersistenceProvider = tenantPersistenceProvider;
    }

    private EntityManagerFactory getDelegate() {
        return tenantPersistenceProvider.getEntityManagerFactory();
    }

    @Override
    public EntityManager createEntityManager() {
        return getDelegate().createEntityManager();
    }

    @Override
    public EntityManager createEntityManager(Map<String, String> properties) {
        return getDelegate().createEntityManager(properties);
    }

    @Override
    public Metamodel getMetamodel() {
        return getDelegate().getMetamodel();
    }

    @Override
    public void close() {
        getDelegate().close();
    }

    @Override
    public boolean isOpen() {
        return getDelegate().isOpen();
    }

    @Override
    public Map<String, String> getProperties() {
        return getDelegate().getProperties();
    }

    @Override
    public Cache getCache() {
        return getDelegate().getCache();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return getDelegate().getPersistenceUnitUtil();
    }

    @Override
    public void addNamedQuery(String name, Query query) {
        getDelegate().addNamedQuery(name, query);
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        if (MultiTenantEntityManagerFactory.class.equals(aClass)) {
            return aClass.cast(this);
        }
        return getDelegate().unwrap(aClass);
    }
}
