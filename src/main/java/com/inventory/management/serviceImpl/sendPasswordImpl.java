package com.inventory.management.serviceImpl;

import com.inventory.management.config.Response;
import com.inventory.management.entity.User;
import com.inventory.management.repository.SendPasswordRepository;
import com.inventory.management.repository.UserRepository;
import com.inventory.management.service.EmailService;
import com.inventory.management.service.SendPasswordService;
import com.inventory.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@Service
public class sendPasswordImpl implements SendPasswordService {

    Random random = new Random(10000000);
    UUID uuid = UUID.randomUUID();


    @Autowired
    EmailService emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SendPasswordRepository sendPasswordRepository;

    @Override
    public Response sendPassword(String email) {
        User user = userRepository.findByEmail(email);
        if(user != null && user.getEmail().equalsIgnoreCase(email)){
            int password = random.nextInt(99999999);
            System.out.println("password " + password);
            String subject ="password from Inventory";
            String message = "password = "+password;
            String to = email;
            this.emailService.sendMailFromGmail(to,subject,subject,null,new ArrayList<>(),null);
            //return new Response(" verify Otp send Successfully on email ", user.getEmail(),HttpStatus.OK);
            return new Response("use password to login, send Successfully on email ", user.getEmail(),HttpStatus.OK);

        }
        return new Response("email is not valid : ",email, HttpStatus.OK);
    }
}
