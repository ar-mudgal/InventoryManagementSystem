package com.inventory.management.controller;

import com.inventory.management.config.Response;
import com.inventory.management.service.SendPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PasswordController {

    @Autowired
    SendPasswordService sendPasswordService;

    @PostMapping("/sendPassword")
    public Response sendPassword(@RequestParam String email){
        return  sendPasswordService.sendPassword(email);
    }

}
