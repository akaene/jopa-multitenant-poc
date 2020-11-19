package com.akaene.flagship.rest;

import com.akaene.flagship.service.repository.TenantService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/admin")
@PreAuthorize("isAuthenticated()")
public class AdminController {

    private final TenantService tenantService;

    public AdminController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping(value = "/storage")
    public List<String> getConnectedRepositories() {
        return tenantService.getConnectedRepositories();
    }
}
