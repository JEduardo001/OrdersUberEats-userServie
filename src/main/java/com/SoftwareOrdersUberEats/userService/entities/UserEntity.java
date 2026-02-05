package com.SoftwareOrdersUberEats.userService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "user_table")
public class UserEntity {


    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private String lastname;
    private LocalDate birthday;
    private Instant createAt;
}