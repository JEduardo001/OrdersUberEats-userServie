package com.SoftwareOrdersUberEats.userService.dto.user;

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
public class DtoUser {

    private UUID id;
    private String name;
    private String lastname;
    private LocalDate birthday;
    private Instant createAt;
}
