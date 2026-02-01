package com.SoftwareOrdersUberEats.userService.controller;

import com.SoftwareOrdersUberEats.userService.constant.ApiBase;
import com.SoftwareOrdersUberEats.userService.dto.responseApi.DtoPageableResponse;
import com.SoftwareOrdersUberEats.userService.dto.responseApi.DtoResponseApi;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUpdateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUser;
import com.SoftwareOrdersUberEats.userService.service.MappedDiagnosticService;
import com.SoftwareOrdersUberEats.userService.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(ApiBase.apiBase + "user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final MappedDiagnosticService mappedDiagnosticService;

    @GetMapping()
    public ResponseEntity<DtoPageableResponse<List<DtoUser>>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll(page,size));
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<DtoResponseApi<?>> getUser(@PathVariable UUID idUser){
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponseApi.builder()
                .status(HttpStatus.OK.value())
                .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                .message("User obtained")
                .data(userService.get(idUser))
                .build());
    }

    @PutMapping()
    public ResponseEntity<DtoResponseApi<?>> updateUser(@Valid @RequestBody DtoUpdateUser request){
        return ResponseEntity.status(HttpStatus.OK).body(DtoResponseApi.builder()
                .status(HttpStatus.OK.value())
                .idCorrelation(mappedDiagnosticService.getIdCorrelation())
                .message("User updated")
                .data(userService.update(request))
                .build());
    }

}
