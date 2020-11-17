package com.akaene.flagship.rest;

import com.akaene.flagship.service.repository.TenantService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/admin")
@PreAuthorize("isAuthenticated()")
public class AdminController {

    private final TenantService tenantService;

    public AdminController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("/storage")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void connectToTenant(@RequestParam String repoUrl) {
        tenantService.connectToTenantRepository(repoUrl);
    }

    @GetMapping(value = "/storage")
    public List<String> getConnectedRepositories() {
        return tenantService.getConnectedRepositories();
    }
}
