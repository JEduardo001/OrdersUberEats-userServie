package com.SoftwareOrdersUberEats.userService.dto.responseApi;

import java.util.List;

public record DtoPageableResponse<T>(
        Long totalElements,
        Integer totalPages,
        List<T> data
) {
}