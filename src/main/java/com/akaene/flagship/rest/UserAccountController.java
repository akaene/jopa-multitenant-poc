package com.akaene.flagship.rest;

import com.akaene.flagship.dto.UserAccountDto;
import com.akaene.flagship.service.UserAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/users")
@PreAuthorize("isAuthenticated()")
public class UserAccountController {

    private final UserAccountService accountService;

    public UserAccountController(UserAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserAccountDto> getUsers() {
        return accountService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserAccountDto account) {
        accountService.create(account);
    }

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccountDto getCurrent() {
        return accountService.getCurrent();
    }
}
