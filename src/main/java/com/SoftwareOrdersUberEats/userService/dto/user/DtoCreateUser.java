package com.SoftwareOrdersUberEats.userService.dto.user;


import com.SoftwareOrdersUberEats.userService.enums.statesResource.StatusResourceUserEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DtoCreateUser {

    @NotNull
    private UUID id;
    @NotEmpty
    private String name;
    private String lastname;
    @NotNull
    private LocalDate birthday;
}