package com.inventory.management.service;

import com.inventory.management.config.Response;
import com.inventory.management.dto.LoginDto;
import com.inventory.management.dto.SendPasswordDto;
import com.inventory.management.dto.UserDto;
import com.inventory.management.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    Response addUser(UserDto userDto, SendPasswordDto sendPasswordDto);

    public List<UserDto> fethAll(UserDto userDto);

    public Response getById(Integer id);

    Response logInUser(LoginDto loginDto);
}
