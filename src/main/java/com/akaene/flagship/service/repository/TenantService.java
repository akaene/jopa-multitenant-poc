package com.akaene.flagship.service.repository;

import com.akaene.flagship.persistence.TenantPersistenceProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    private final TenantPersistenceProvider tenantPersistenceProvider;

    public TenantService(TenantPersistenceProvider tenantPersistenceProvider) {
        this.tenantPersistenceProvider = tenantPersistenceProvider;
    }

    public void connectToTenantRepository(String repoUrl) {
        tenantPersistenceProvider.connectTo(repoUrl);
    }

    public List<String> getConnectedRepositories() {
        return tenantPersistenceProvider.getConnectedRepositories();
    }
}
