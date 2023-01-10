package com.inventory.management.serviceImpl;

import com.inventory.management.config.Response;
import com.inventory.management.entity.SendPassword;
import com.inventory.management.entity.User;
import com.inventory.management.repository.SendPasswordRepository;
import com.inventory.management.repository.UserRepository;
import com.inventory.management.service.EmailService;
import com.inventory.management.service.SendPasswordService;
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
//        int password = 0;
//        password = random.nextInt(99999);
          int pass = generatePassword();
          User user1 = new User();
        if (user != null && user.getEmail().equalsIgnoreCase(email)) {
            System.out.println("password " +pass);
            String subject = "password from Inventory";
            String message = "password = " + pass;
            String to = email;
            this.emailService.sendMailFromGmail(to,subject,subject,null,new ArrayList<>(),null);
//            this.emailService.sendEmail(subject, message, to);
//            SendPassword sendPassword = new SendPassword();
//            sendPassword.setPassword(String.valueOf(pass));
//            sendPassword.setEmail(user.getEmail());
//            sendPasswordRepository.save(sendPassword);
            user.setPassword(String.valueOf(pass));
            userRepository.save(user);
            //return new Response(" verify Otp send Successfully on email ", user.getEmail(),HttpStatus.OK);
            return new Response("use password to login send Successfully on email ", user.getEmail(), HttpStatus.OK);
        }
        return new Response("email is not valid : ", email, HttpStatus.OK);
    }

    public int generatePassword(){
        int password=0;
        password = random.nextInt(999999);
        return password;
    }

}
