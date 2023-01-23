package com.inventory.management.controller;

import com.inventory.management.config.Response;
import com.inventory.management.dto.LoginDto;
import com.inventory.management.dto.SendPasswordDto;
import com.inventory.management.dto.UserDto;
import com.inventory.management.repository.UserRepository;
import com.inventory.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    Response addUser(@Valid @RequestBody UserDto userDto, SendPasswordDto sendPasswordDto){

        return userService.addUser(userDto,sendPasswordDto);
    }

    @GetMapping("/all")
    public List<UserDto> fethAll(UserDto userDto){
        return userService.fethAll(userDto);
    }

    @GetMapping("/getById/{id}")
    public Response getById(@PathVariable Integer id){
        return userService.getById(id);
    }

    @PostMapping("/login")
    public Response logInUser(@RequestBody LoginDto loginDto) throws UnsupportedEncodingException {
        return userService.logInUser(loginDto);
    }

    @DeleteMapping("/delete/{id}")
    public Response deleteUser(@PathVariable Integer id){
        return userService.deleateUser(id);
    }

    @PostMapping("/update")
    public Response upadateUser(@RequestBody @Valid UserDto uderDto) throws Exception {
        return userService.upadateUser(uderDto);
    }

    @PutMapping("/changePassword")
    public Response chnagePasswordgePassword(@RequestBody UserDto userDto){
        return userService.changePassword(userDto);
    }

}