package com.SoftwareOrdersUberEats.userService;

import com.SoftwareOrdersUberEats.userService.dto.responseApi.DtoPageableResponse;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUpdateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUser;
import com.SoftwareOrdersUberEats.userService.entities.UserEntity;
import com.SoftwareOrdersUberEats.userService.enums.statesCreateResource.ResultEventEnum;
import com.SoftwareOrdersUberEats.userService.exception.user.UserNotFoundException;
import com.SoftwareOrdersUberEats.userService.mapper.UserMapper;
import com.SoftwareOrdersUberEats.userService.repository.UserRepository;
import com.SoftwareOrdersUberEats.userService.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return pageable users")
    void getAll_Success() {
        UserEntity entity = new UserEntity();
        Page<UserEntity> page = new PageImpl<>(List.of(entity));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(userMapper.toDto(any())).thenReturn(DtoUser.builder().build());

        DtoPageableResponse<List<DtoUser>> result = userService.getAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.totalElements());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void get_NotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.get(id));
    }

    @Test
    @DisplayName("Should return CREATED when user data is valid and unique")
    void create_Success() {
        UUID userId = UUID.randomUUID();
        DtoCreateUser request = DtoCreateUser.builder().id(userId).build();
        UserEntity entity = new UserEntity();

        when(validator.validate(request)).thenReturn(Collections.emptySet());
        when(userRepository.existsById(userId)).thenReturn(false);
        when(userMapper.toEntityToCreate(request)).thenReturn(entity);

        ResultEventEnum result = userService.create(request);

        assertEquals(ResultEventEnum.CREATED, result);
        verify(userRepository).save(entity);
        verify(userRepository).flush();
    }

    @Test
    @DisplayName("Should return VALIDATION_ERROR when constraint violations exist")
    void create_ValidationError() {
        DtoCreateUser request = DtoCreateUser.builder().build();
        ConstraintViolation<DtoCreateUser> violation = mock(ConstraintViolation.class);
        when(validator.validate(request)).thenReturn(Set.of(violation));

        ResultEventEnum result = userService.create(request);

        assertEquals(ResultEventEnum.VALIDATION_ERROR, result);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return ALREADY_EXISTS when ID is already in database")
    void create_AlreadyExists() {
        UUID userId = UUID.randomUUID();
        DtoCreateUser request = DtoCreateUser.builder().id(userId).build();

        when(validator.validate(request)).thenReturn(Collections.emptySet());
        when(userRepository.existsById(userId)).thenReturn(true);

        ResultEventEnum result = userService.create(request);

        assertEquals(ResultEventEnum.ALREADY_EXISTS, result);
    }

    @Test
    @DisplayName("Should return ALREADY_EXISTS when DataIntegrityViolationException occurs")
    void create_DataIntegrityError() {
        DtoCreateUser request = DtoCreateUser.builder().id(UUID.randomUUID()).build();
        when(validator.validate(request)).thenReturn(Collections.emptySet());
        when(userRepository.existsById(any())).thenReturn(false);
        when(userMapper.toEntityToCreate(any())).thenReturn(new UserEntity());
        doThrow(DataIntegrityViolationException.class).when(userRepository).save(any());

        ResultEventEnum result = userService.create(request);

        assertEquals(ResultEventEnum.ALREADY_EXISTS, result);
    }

    @Test
    @DisplayName("Should update user successfully")
    void update_Success() {
        UUID userId = UUID.randomUUID();
        DtoUpdateUser request = DtoUpdateUser.builder().id(userId).build();
        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenReturn(existingUser);
        when(userMapper.toDto(any())).thenReturn(DtoUser.builder().id(userId).build());

        DtoUser result = userService.update(request);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userMapper).toEntityToUpdate(request, existingUser);
        verify(userRepository).save(existingUser);
    }
}