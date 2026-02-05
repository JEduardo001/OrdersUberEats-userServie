package com.SoftwareOrdersUberEats.userService.repository;

import com.SoftwareOrdersUberEats.userService.entities.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, UUID> {
}

