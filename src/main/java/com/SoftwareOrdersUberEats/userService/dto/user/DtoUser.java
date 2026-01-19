package com.SoftwareOrdersUberEats.userService.dto.user;

import com.SoftwareOrdersUberEats.userService.enums.StatusResourceUser;
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
    private StatusResourceUser status;
    private Instant createAt;
    private Instant disableAt;
}
