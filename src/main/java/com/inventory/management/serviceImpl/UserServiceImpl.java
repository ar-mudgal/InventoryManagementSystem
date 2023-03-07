package com.inventory.management.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.management.config.Response;
import com.inventory.management.dto.LoginDto;
import com.inventory.management.dto.SendPasswordDto;
import com.inventory.management.dto.UserDto;
import com.inventory.management.entity.Rating;
import com.inventory.management.entity.Role;
import com.inventory.management.entity.User;
import com.inventory.management.repository.UserRepository;
import com.inventory.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

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
        user.setPassword(sendPasswordDto.getPassword());   //sended password added in user table
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
            dt = new UserDto();
            dt.setUserId(user.getUserId());
            dt.setName(user.getName());
            dt.setEmail(user.getEmail());
            dt.setGender(user.getGender());
            dt.setMobile(user.getMobile());
            dt.setAddress(user.getAddress());
            dt.setPincode(user.getPincode());
            dt.setPassword(user.getPassword());
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
//        Base64.Decoder decoder = Base64.getDecoder();
        String encodedString = Base64.getEncoder().encodeToString(loginDto.getPassword().getBytes());

        // Decoding string
//        String dStr = new String(decoder.decode(loginDto.getPassword().getBytes(StandardCharsets.UTF_8)));

        User user = userRepository.findByEmailAndPassword(loginDto.getEmail(), encodedString);
//        User user = userRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
       UserDto dto = new UserDto();
       if(user==null){
           return new Response("invalid credentials login id or password not match",HttpStatus.BAD_REQUEST);
       }
//        System.out.println(encodedString);
//        System.out.println("DataBase password : "+user.getPassword());
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
           user.setName(uderDto.getName());
           user.setAddress(uderDto.getAddress());
           user.setGender(uderDto.getGender());
           user.setEmail(uderDto.getEmail());
           user.setMobile(uderDto.getMobile());
           user.setPincode(uderDto.getPincode());
           String encodedString = Base64.getEncoder().encodeToString(uderDto.getPassword().getBytes());
           user.setPassword(encodedString);
           userRepository.save(user);
           return new Response("user updated successfully", HttpStatus.OK);
       }
        return new Response("user not found for this id", uderDto.getUserId(),HttpStatus.BAD_REQUEST);
    }

    @Override
    public Response changePassword(UserDto userDto) {
        Optional<User> userOptional = userRepository.findById(userDto.getUserId());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            String encodedString = Base64.getEncoder().encodeToString(userDto.getPassword().getBytes());
            user.setPassword(encodedString);
            userRepository.save(user);
            return new Response("Password updated successfully",HttpStatus.OK);
        }
        return new Response("user not found for this id : ",HttpStatus.BAD_REQUEST);
    }

}
