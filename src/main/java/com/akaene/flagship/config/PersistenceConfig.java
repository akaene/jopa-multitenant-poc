package com.akaene.flagship.config;

import com.akaene.flagship.persistence.tenant.MultiTenantEntityManagerFactory;
import com.akaene.flagship.persistence.tenant.TenantPersistenceProvider;
import com.github.ledsoft.jopa.spring.transaction.DelegatingEntityManager;
import com.github.ledsoft.jopa.spring.transaction.JopaTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class PersistenceConfig {

    @Bean
    @Primary
    public DelegatingEntityManager entityManager() {
        return new DelegatingEntityManager();
    }

    @Bean("multitenantEMF")
    public MultiTenantEntityManagerFactory multiTenantEntityManagerFactory(
            TenantPersistenceProvider tenantPersistenceProvider) {
        return new MultiTenantEntityManagerFactory(tenantPersistenceProvider);
    }

    @Bean(name = "txManager")
    public PlatformTransactionManager transactionManager(MultiTenantEntityManagerFactory emf,
                                                         DelegatingEntityManager emProxy) {
        return new JopaTransactionManager(emf, emProxy);
    }
}
