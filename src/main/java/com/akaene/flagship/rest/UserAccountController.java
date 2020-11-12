package com.akaene.flagship.rest;

import com.akaene.flagship.exception.NotFoundException;
import com.akaene.flagship.model.UserAccount;
import com.akaene.flagship.service.UserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/users")
public class UserAccountController {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccountController.class);

    private final UserAccountService accountService;

    public UserAccountController(UserAccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserAccount> getUsers() {
        return accountService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody UserAccount account) {
        accountService.create(account);
        LOG.debug("Created user {}", account);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UserAccount getByUsername(@RequestParam String username) {
        final UserAccount result = accountService.findByUsername(username);
        if (result == null) {
            throw NotFoundException.create(UserAccount.class, username);
        }
        return result;
    }
}
