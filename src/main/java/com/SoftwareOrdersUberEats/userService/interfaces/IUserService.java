package com.SoftwareOrdersUberEats.userService.interfaces;

import com.SoftwareOrdersUberEats.userService.dto.responseApi.DtoPageableResponse;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUpdateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUser;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    DtoPageableResponse<List<DtoUser>> getAll(int page, int size);
    DtoUser get(UUID id);
    DtoUser update(DtoUpdateUser id);
    void create(DtoCreateUser request);
}
