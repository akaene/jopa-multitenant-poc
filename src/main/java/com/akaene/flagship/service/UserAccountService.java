package com.akaene.flagship.service;

import com.akaene.flagship.dto.UserAccountDto;
import com.akaene.flagship.dto.mapper.UserAccountMapper;
import com.akaene.flagship.exception.ValidationException;
import com.akaene.flagship.model.UserAccount;
import com.akaene.flagship.service.repository.UserAccountRepositoryService;
import com.akaene.flagship.service.security.SecurityUtils;
import com.akaene.flagship.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccountService.class);

    private final Validator validator;

    private final UserAccountMapper mapper;

    private final UserAccountRepositoryService repositoryService;

    public UserAccountService(Validator validator, UserAccountMapper mapper,
                              UserAccountRepositoryService repositoryService) {
        this.validator = validator;
        this.mapper = mapper;
        this.repositoryService = repositoryService;
    }

    @Transactional
    public void create(UserAccountDto account) {
        Objects.requireNonNull(account);
        final UserAccount entity = mapper.dtoToUserAccount(account);
        validate(entity);
        repositoryService.persist(entity);
        LOG.debug("Created user {}", entity);
    }

    public List<UserAccountDto> findAll() {
        return repositoryService.findAll().stream().map(mapper::userAccountToDto).collect(Collectors.toList());
    }

    public UserAccountDto getCurrent() {
        return mapper.userAccountToDto(
                repositoryService.findByUsername(SecurityUtils.getCurrentUserDetails().getUsername()));
    }

    /**
     * Validates the specified instance, using JSR 380.
     * <p>
     * This assumes that the type contains JSR 380 validation annotations.
     *
     * @param instance The instance to validate
     * @throws ValidationException In case the instance is not valid
     */
    protected void validate(UserAccount instance) {
        final ValidationResult<UserAccount> validationResult = ValidationResult.of(validator.validate(instance));
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult);
        }
    }
}
