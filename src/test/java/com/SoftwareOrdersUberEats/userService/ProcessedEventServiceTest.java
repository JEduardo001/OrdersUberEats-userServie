package com.SoftwareOrdersUberEats.userService;

import com.SoftwareOrdersUberEats.userService.entities.ProcessedEventEntity;
import com.SoftwareOrdersUberEats.userService.repository.ProcessedEventRepository;
import com.SoftwareOrdersUberEats.userService.service.ProcessedEventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessedEventServiceTest {

    @Mock
    private ProcessedEventRepository processedEventRepository;

    @InjectMocks
    private ProcessedEventService processedEventService;

    @Test
    @DisplayName("Should save processed event successfully")
    void save_Success() {
        UUID eventId = UUID.randomUUID();

        processedEventService.save(eventId);

        verify(processedEventRepository).save(any(ProcessedEventEntity.class));
    }

    @Test
    @DisplayName("Should not throw exception when event already exists (duplicate key)")
    void save_ShouldHandleException_WhenEventAlreadyExists() {
        UUID eventId = UUID.randomUUID();
        doThrow(new RuntimeException("Duplicate entry")).when(processedEventRepository).save(any());

        // El servicio captura la excepción con try-catch, por lo que no debe relanzarla
        assertDoesNotThrow(() -> processedEventService.save(eventId));
        verify(processedEventRepository).save(any());
    }

    @Test
    @DisplayName("Should return true if event was already processed")
    void isEventProcessed_True() {
        UUID eventId = UUID.randomUUID();
        when(processedEventRepository.existsById(eventId)).thenReturn(true);

        boolean result = processedEventService.isEventProcessed(eventId);

        assertTrue(result);
    }

    @Test
    @DisplayName("Should return false if event has not been processed")
    void isEventProcessed_False() {
        UUID eventId = UUID.randomUUID();
        when(processedEventRepository.existsById(eventId)).thenReturn(false);

        boolean result = processedEventService.isEventProcessed(eventId);

        assertFalse(result);
    }
}