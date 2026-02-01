package com.SoftwareOrdersUberEats.userService.dto.responseApi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class DtoResponseApiWithoutData {
    Integer status;
    String message;
    String correlationId;

}
