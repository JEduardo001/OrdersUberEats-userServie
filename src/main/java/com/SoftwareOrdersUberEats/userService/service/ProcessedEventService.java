package com.SoftwareOrdersUberEats.userService.service;

import com.SoftwareOrdersUberEats.userService.entities.ProcessedEventEntity;
import com.SoftwareOrdersUberEats.userService.repository.ProcessedEventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static com.SoftwareOrdersUberEats.userService.constant.TracerConstants.EXCEPTION_ALREADY_EVENT_PROCESSED;

@Service
@AllArgsConstructor
@Slf4j
public class ProcessedEventService {

    private ProcessedEventRepository processedEventRepository;

    public void save(UUID id){
        try{
            processedEventRepository.save(ProcessedEventEntity.builder()
                    .id(id)
                    .processedAt(Instant.now())
                    .build());
        }catch(Exception e){
            log.warn(EXCEPTION_ALREADY_EVENT_PROCESSED, id);
        }
    }

    public boolean isEventProcessed(UUID id){
        return processedEventRepository.existsById(id);
    }
}
