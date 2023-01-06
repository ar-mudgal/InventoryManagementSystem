package com.inventory.management.serviceImpl;

import com.inventory.management.config.Response;
import com.inventory.management.dto.LoginDto;
import com.inventory.management.dto.SendPasswordDto;
import com.inventory.management.dto.UserDto;
import com.inventory.management.entity.Role;
import com.inventory.management.entity.User;
import com.inventory.management.repository.UserRepository;
import com.inventory.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.tags.form.OptionsTag;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;




    @Override
    public Response addUser(UserDto userDto, SendPasswordDto sendPasswordDto) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));
        if(userOptional.isPresent()){
            try {
                throw new Exception("User already exist");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Role role = new Role();
        role.setUser(userDto.getRole().getUser());
        role.setAdmin(userDto.getRole().getAdmin());
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setMobile(userDto.getMobile());
        user.setGender(userDto.getGender());
        user.setPincode(userDto.getPincode());
        user.setPassword(sendPasswordDto.getPassword());
//        UUID uuid = UUID.randomUUID();
//        user.setPassword(uuid.toString());
        user.setAddress(userDto.getAddress());
        user.setRole(role);
        userRepository.save(user);
        return new Response("User added successfully", HttpStatus.OK);
    }

    @Override
    public List<UserDto> fethAll(UserDto userDto){
        List<UserDto> dtoList = new ArrayList<>();
        List<User> usrList = userRepository.findAll();
        for(User ur : usrList){
            UserDto dto = new UserDto();
            dto.setId(ur.getId());
            dto.setName(ur.getName());
            dto.setMobile(ur.getMobile());
            dto.setGender(ur.getGender());
            dto.setEmail(ur.getEmail());
            dto.setAddress(ur.getAddress());
            dto.setPincode(ur.getPincode());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public Response getById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        UserDto dt = null;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            dt = new UserDto();
            dt.setId(user.getId());
            dt.setName(user.getName());
            dt.setEmail(user.getEmail());
            dt.setGender(user.getGender());
            dt.setMobile(user.getMobile());
            dt.setAddress(user.getAddress());
            dt.setPincode(user.getPincode());
        }
        return new Response("user fond successfully", dt,HttpStatus.OK);
    }

    @Override
    public Response logInUser(LoginDto loginDto) {
       User user = userRepository.findByEmailAndPassword(loginDto.getEmail(),loginDto.getPassword());
       UserDto dto = new UserDto();
       if(user==null){
           return new Response("invalid crediantials login id or password not match",HttpStatus.BAD_REQUEST);
       }
       dto.setId(user.getId());
       dto.setName(user.getName());
       dto.setPincode(user.getPincode());
       dto.setAddress(user.getAddress());
       dto.setMobile(user.getMobile());
       dto.setPassword(user.getPassword());
       dto.setGender(user.getGender());
       dto.setEmail(user.getEmail());
        return new Response("Login successfully", dto,HttpStatus.OK);
    }


    private static char[] generatePassword(int length) {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        char[] password = new char[length];

        password[0] = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password[1] = capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password[2] = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password[3] = numbers.charAt(random.nextInt(numbers.length()));

        for (int i = 4; i < length; i++) {
            password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;

    }

    }
