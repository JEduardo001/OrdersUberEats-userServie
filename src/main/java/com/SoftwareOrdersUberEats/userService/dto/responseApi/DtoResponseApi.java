package com.SoftwareOrdersUberEats.userService.dto.responseApi;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class DtoResponseApi<T> {
    private Integer status;
    private String message;
    private T data;
}
