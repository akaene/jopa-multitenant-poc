package com.akaene.flagship.service.repository;

import com.akaene.flagship.persistence.tenant.TenantPersistenceProvider;
import com.akaene.flagship.util.HttpUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    private final TenantPersistenceProvider tenantPersistenceProvider;

    private final HttpUtils httpUtils;

    public TenantService(TenantPersistenceProvider tenantPersistenceProvider,
                         HttpUtils httpUtils) {
        this.tenantPersistenceProvider = tenantPersistenceProvider;
        this.httpUtils = httpUtils;
    }

    public void connectToTenantRepository() {
        // In the real application, tenantId will be used to retrieve repository URL from the main system repository
        tenantPersistenceProvider.connectTo(httpUtils.resolveTenant().toString());
    }

    public List<String> getConnectedRepositories() {
        return tenantPersistenceProvider.getConnectedRepositories();
    }
}
