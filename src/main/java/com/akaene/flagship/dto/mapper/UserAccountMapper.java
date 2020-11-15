package com.akaene.flagship.dto.mapper;

import com.akaene.flagship.dto.UserAccountDto;
import com.akaene.flagship.model.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    UserAccountDto userAccountToDto(UserAccount account);

    UserAccount dtoToUserAccount(UserAccountDto dto);
}
