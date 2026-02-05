package com.SoftwareOrdersUberEats.userService.exception.exceptionHandler;

import com.SoftwareOrdersUberEats.userService.dto.responseApi.DtoResponseApiWithoutData;
import com.SoftwareOrdersUberEats.userService.exception.user.UserNotFoundException;
import com.SoftwareOrdersUberEats.userService.service.MappedDiagnosticService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(1)
@Slf4j
@AllArgsConstructor
public class UserExceptionHandler {

    private final MappedDiagnosticService mappedDiagnosticService;

    private ResponseEntity<DtoResponseApiWithoutData> buildResponse(HttpStatus status, String message, Exception ex) {
        log.warn("Business exception: {} - Message: {}", ex.getClass().getSimpleName(), message);

        return ResponseEntity.status(status)
                .body(DtoResponseApiWithoutData.builder()
                        .status(status.value())
                        .message(message)
                        .correlationId(mappedDiagnosticService.getIdCorrelation())
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DtoResponseApiWithoutData> userNotFoundException(UserNotFoundException ex){
        return buildResponse(HttpStatus.NOT_FOUND, "User not found", ex);
    }
}
