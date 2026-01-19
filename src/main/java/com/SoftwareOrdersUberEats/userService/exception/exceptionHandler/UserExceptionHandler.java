package com.SoftwareOrdersUberEats.userService.exception.exceptionHandler;

import com.SoftwareOrdersUberEats.userService.dto.responseApi.DtoResponseApiWithoutData;
import com.SoftwareOrdersUberEats.userService.exception.user.UserNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(1)
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DtoResponseApiWithoutData> userNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DtoResponseApiWithoutData.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("User not found")
                .build());
    }
}
