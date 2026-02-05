package com.SoftwareOrdersUberEats.userService.mapper;

import com.SoftwareOrdersUberEats.userService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUpdateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUser;
import com.SoftwareOrdersUberEats.userService.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    DtoUser toDto(UserEntity user);
    UserEntity toEntityToCreate(DtoCreateUser request);
    @Mapping(target = "id", ignore = true)
    void toEntityToUpdate(DtoUpdateUser newData,@MappingTarget UserEntity actualData);
}
