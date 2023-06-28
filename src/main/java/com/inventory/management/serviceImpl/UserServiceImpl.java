package com.inventory.management.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.management.config.Response;
import com.inventory.management.dto.LoginDto;
import com.inventory.management.dto.SendPasswordDto;
import com.inventory.management.dto.UserDto;
import com.inventory.management.entity.Role;
import com.inventory.management.entity.User;
import com.inventory.management.repository.RoleRepository;
import com.inventory.management.repository.UserRepository;
import com.inventory.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response addUser(UserDto userDto, SendPasswordDto sendPasswordDto) {
        Optional<User> userOptional = userRepository.findByEmail(userDto.getEmail());
        if(userOptional.isPresent()){
            try {
                throw new Exception("User already exist");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Role role = new Role();
        role.setRoleName(userDto.getRole().getRoleName());
        User user=User.builder().email(userDto.getEmail()).gender(userDto.getGender()).role(role).address(userDto.getAddress()).pincode(userDto.getPincode()).name(userDto.getName()).password(sendPasswordDto.getPassword()).mobile(userDto.getMobile()).build();
        role.setUser(user);
        userRepository.save(user);
        return new Response("User added successfully", HttpStatus.OK);
    }

    @Override
    public List<UserDto> fethAll(UserDto userDto){
        List<UserDto> dtoList = new ArrayList<>();
        List<User> usrList = userRepository.findAll(Sort.by("LENGTH(userDto.getUserName)"));
        for(User ur : usrList){
            UserDto dto = new UserDto();
            dto.setUserId(ur.getUserId());
            dto.setName(ur.getName());
            dto.setMobile(ur.getMobile());
            dto.setGender(ur.getGender());
            dto.setEmail(ur.getEmail());
            dto.setAddress(ur.getAddress());
            dto.setPincode(ur.getPincode());
//            ArrayList<Rating> ratingOfUser = restTemplate.getForObject("http://localhost:9091/rating/getByUserId/"+ur.getUserId(), ArrayList.class);
//            log.info("Ratings invoked {} ", ratingOfUser);
//            dto.setRating(ratingOfUser);
            dtoList.add(dto);
        }
        return dtoList;
    }


    //find user by id
    @Override
    public Response getById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            return  new Response("user not found for id {}", id,HttpStatus.BAD_REQUEST);
        }
        UserDto dt = null;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
//            UserDto.builder().userId(user.getUserId()).address(user.getAddress()).email(user.getEmail()).gender(user.getGender()).mobile(user.getMobile()).name(user.getName()).pincode(user.getPincode()).password(user.getPassword()).build();
            dt = new UserDto();
            dt.setUserId(user.getUserId());
            dt.setName(user.getName());
            dt.setEmail(user.getEmail());
            dt.setGender(user.getGender());
            dt.setMobile(user.getMobile());
            dt.setAddress(user.getAddress());
            dt.setPincode(user.getPincode());
            dt.setPassword(user.getPassword());
            Role role = new Role();
//            for(Role user1 : user.getRole()){
//                role.setUser(user1.getUser());
//                role.setRoleName(user1.getRoleName());
////                role.setRole_id(user1.getRole_id());
//            }
            dt.setRole(user.getRole());

//            fetch ratings of the user from rating service
//            ArrayList<Rating> ratingOfUser = restTemplate.getForObject("http://localhost:9091/rating/getByUserId/"+user.getUserId(), ArrayList.class);
//            log.info("Ratings invoked {} ", ratingOfUser);
//            dt.setRating(ratingOfUser);
        }
        return new Response("user fond successfully", dt,HttpStatus.OK);
    }

    @Autowired
    ObjectMapper mapper;
    @Override
    public Response logInUser(LoginDto loginDto) throws UnsupportedEncodingException {
        Response response = new Response();
        String encodedString = Base64.getEncoder().encodeToString(loginDto.getPassword().getBytes());
        Optional<User> email = userRepository.findByEmail(loginDto.getUserName());
        List<User> mob = userRepository.findByMobile(loginDto.getUserName());
        User user = null;
        if(email.isPresent()) {
            user = userRepository.findByEmailAndPassword(loginDto.getUserName(), encodedString);
        }
        if (!CollectionUtils.isEmpty(mob)) {
            List<String > mobiles = mob.stream().map(User::getMobile).collect(Collectors.toList());
            for(String mobile : mobiles) {
                user = userRepository.findByMobileAndPassword(mobile, encodedString);
                if(user != null) {
                    break;
                }
            }
        }

       UserDto dto = new UserDto();
       if(user==null){
           return new Response("invalid credentials login id or password not match",HttpStatus.BAD_REQUEST);
       }
       dto.setUserId(user.getUserId());
       dto.setName(user.getName());
       dto.setPincode(user.getPincode());
       dto.setAddress(user.getAddress());
       dto.setMobile(user.getMobile());
       dto.setPassword(user.getPassword());
       dto.setGender(user.getGender());
       dto.setEmail(user.getEmail());

       try {
           mapper.writeValueAsString(dto);
       }
       catch (Exception e){
           System.out.println(e);
       }
        return new Response("Login successfully", dto,HttpStatus.OK);
    }

    @Override
    public Response deleateUser(Integer id) {
        Optional<User> usrOptional = userRepository.findById(id);
        if(usrOptional.isPresent()){
            userRepository.deleteById(id);
            return new Response("user deleted Successfully", id, HttpStatus.OK);
        }
        return new Response("User not found", HttpStatus.BAD_REQUEST);
    }

    @Override
    public Response upadateUser(UserDto uderDto) throws Exception {
       Optional<User> userOptional = userRepository.findById(uderDto.getUserId());
       if(userOptional.isPresent()){
           User user = userOptional.get();
//           user.builder().name(uderDto.getName()).address(uderDto.getAddress()).gender(uderDto.getGender()).email(uderDto.getEmail()).password(encodedString).mobile(uderDto.getMobile()).pincode(uderDto.getPincode()).build();
           user.setName(uderDto.getName());
           user.setAddress(uderDto.getAddress());
           user.setGender(uderDto.getGender());
           user.setEmail(uderDto.getEmail());
           user.setMobile(uderDto.getMobile());
           user.setPincode(uderDto.getPincode());
           String encodedString = Base64.getEncoder().encodeToString(uderDto.getPassword().getBytes());
           user.setPassword(encodedString);
           Role role = new Role();
           role.setRoleName(uderDto.getRole().getRoleName());
           role.setUser(user);
           user.setRole(role);
           userRepository.save(user);
           return new Response("user updated successfully", HttpStatus.OK);
       }
        return new Response("user not found for this id", uderDto.getUserId(),HttpStatus.BAD_REQUEST);
    }

    @Override
    public Response changePassword(UserDto userDto) {
        log.info("request to change password invoked successfully for user id -{}",userDto.getUserId());
        Optional<User> userOptional = userRepository.findById(userDto.getUserId());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            String encodedString = Base64.getEncoder().encodeToString(userDto.getPassword().getBytes());
            user.setPassword(encodedString);
            userRepository.save(user);
            log.info("password updated successfully");
            return new Response("Password updated successfully",HttpStatus.OK);
        }
        log.info("user not found for id -{}",userDto.getUserId());
        return new Response("user not found for this id : ",HttpStatus.BAD_REQUEST);
    }

    //get All users by using pagination and without pagination.
    @Override
    public List<User> getDataforPaging(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<User> pagedUsers = userRepository.findAll(pageable);
        if (pagedUsers.hasContent()){
            log.info("data get successfully with pagination");
            return pagedUsers.getContent();
        }else {
            log.info("data get successfully without pagination");
            return  new ArrayList<>();
        }
    }

    @Override
    public List<User> getUsers(String name) {
        log.info("request to getUser method initiated successfully");
        List<User> users = userRepository.findByName(name);
        if(!CollectionUtils.isEmpty(users)){
            log.info("get successfully user list by name wise and name ->{}",name);
            return users;
        }
        log.info("get all user list successfully");
        return userRepository.findAll();
    }

    @Override
    public List<?> findByName(String name) throws Exception {
        List<User> users = userRepository.findByName(name);
        if(CollectionUtils.isEmpty(users)){
            throw new Exception(HttpStatus.BAD_REQUEST.toString());
        }
        log.info("get all users by name wise successfully");
        return users;
    }

}
