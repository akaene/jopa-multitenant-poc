package com.akaene.flagship.persistence;

import com.akaene.flagship.persistence.tenant.TenantPersistenceProvider;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class MainPersistenceFactory {

    private final Environment environment;

    private final TenantPersistenceProvider provider;

    private EntityManagerFactory emf;

    public MainPersistenceFactory(Environment environment, TenantPersistenceProvider provider) {
        this.environment = environment;
        this.provider = provider;
    }

    @Bean("mainEmf")
    @Primary
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @PostConstruct
    private void init() {
        this.emf = provider.connectTo(environment.getRequiredProperty("repository.url"));
    }

    @PreDestroy
    private void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}
