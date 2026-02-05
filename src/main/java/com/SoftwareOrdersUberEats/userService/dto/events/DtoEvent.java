package com.SoftwareOrdersUberEats.userService.dto.events;

import com.SoftwareOrdersUberEats.userService.enums.statesCreateResource.ResultEventEnum;
import com.SoftwareOrdersUberEats.userService.enums.typeEvents.TypeEventEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DtoEvent<T> {
    private TypeEventEnum typeEvent;
    private ResultEventEnum resultEvent;
    private UUID idEvent;
    private String correlationId;
    private T data;
}
