package com.SoftwareOrdersUberEats.userService.service;

import com.SoftwareOrdersUberEats.userService.dto.responseApi.DtoPageableResponse;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoCreateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUpdateUser;
import com.SoftwareOrdersUberEats.userService.dto.user.DtoUser;
import com.SoftwareOrdersUberEats.userService.entities.UserEntity;
import com.SoftwareOrdersUberEats.userService.enums.StatusResourceUser;
import com.SoftwareOrdersUberEats.userService.exception.user.UserNotFoundException;
import com.SoftwareOrdersUberEats.userService.interfaces.IUserService;
import com.SoftwareOrdersUberEats.userService.mapper.UserMapper;
import com.SoftwareOrdersUberEats.userService.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
    public void create(DtoCreateUser request){
       //pending

    }

    @Override
    public DtoUser update(DtoUpdateUser request){
        UserEntity actualDataUser = getUser(request.getId());

        actualDataUser.setDisableAt( request.getStatus().equals(StatusResourceUser.DISABLE) ? Instant.now() : null);
        userMapper.toEntityToUpdate(request,actualDataUser);

        return userMapper.toDto(userRepository.save(actualDataUser));
    }

}

