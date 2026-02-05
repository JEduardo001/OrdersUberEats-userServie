package com.SoftwareOrdersUberEats.userService.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.dao.DataIntegrityViolationException;

import com.SoftwareOrdersUberEats.userService.dto.responseApi.DtoPageableResponse;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUpdateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUser;
import com.SoftwareOrdersUberEats.userService.entities.UserEntity;
import com.SoftwareOrdersUberEats.userService.enums.statesCreateResource.ResultEventEnum;
import com.SoftwareOrdersUberEats.userService.exception.user.UserNotFoundException;
import com.SoftwareOrdersUberEats.userService.interfaces.IUserService;
import com.SoftwareOrdersUberEats.userService.mapper.UserMapper;
import com.SoftwareOrdersUberEats.userService.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.SoftwareOrdersUberEats.userService.constant.TracerConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator validator;


    private UserEntity getUser(UUID id){
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public DtoPageableResponse<List<DtoUser>> getAll(int page, int size){
        Page<UserEntity> users = userRepository.findAll(PageRequest.of(page,size));
        List<DtoUser> usersDto = users.getContent().stream().map(userMapper::toDto).
                collect(Collectors.toList());

        return new DtoPageableResponse(
                users.getTotalElements(),
                users.getTotalPages(),
                usersDto
        );
    }

    @Override
    public DtoUser get(UUID id){
        return userMapper.toDto(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    @Transactional(noRollbackFor = {DataIntegrityViolationException.class, ObjectOptimisticLockingFailureException.class})
    public ResultEventEnum create(DtoCreateUser request) {
        try {

            Set<ConstraintViolation<DtoCreateUser>> violations = validator.validate(request);

            if (!violations.isEmpty()) {
                log.info(MESSAGE_DATA_VALIDATION_TO_CREATE_USER_ERROR);
                return ResultEventEnum.VALIDATION_ERROR;
            }

            if (userRepository.existsById(request.getId())) {
                return ResultEventEnum.ALREADY_EXISTS;
            }
            UserEntity user = userMapper.toEntityToCreate(request);
            user.setCreateAt(Instant.now());

            userRepository.save(user);
            userRepository.flush();
            log.info(MESSAGE_SAVE_USER);

            return ResultEventEnum.CREATED;
        } catch (DataIntegrityViolationException | ObjectOptimisticLockingFailureException e) {
            return ResultEventEnum.ALREADY_EXISTS;
        }
    }


    @Override
    public DtoUser update(DtoUpdateUser request){
        UserEntity actualDataUser = getUser(request.getId());

        userMapper.toEntityToUpdate(request,actualDataUser);
        UserEntity user = userRepository.save(actualDataUser);

        log.info(MESSAGE_UPDATE_USER, user.getId());

        return userMapper.toDto(user);
    }

}

