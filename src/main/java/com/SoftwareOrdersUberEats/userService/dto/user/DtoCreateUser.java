package com.SoftwareOrdersUberEats.userService.dto.user;


import com.SoftwareOrdersUberEats.userService.enums.StatusResourceUser;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DtoCreateUser {

    @NotEmpty
    private String name;
    private String lastname;
    @NotNull
    private LocalDate birthday;
    @NotNull
    private StatusResourceUser status;
}