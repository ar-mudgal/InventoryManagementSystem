package com.inventory.management.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inventory.management.entity.Rating;
import com.inventory.management.entity.Role;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Integer userId;

    @NotEmpty(message = "name can't be null")
    private String name;

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Size(min = 8, message = "Password length must be minimum 8")
    private String password;

    private String gender;

    @NotNull(message = "address can't be empty")
    private String address;

    @Size(min = 10, max = 10, message = "enter valid mobile number")
    private String mobile;

    @Size(min = 6, message = "pin should be 6 digit long")
    private String pincode;

    private Role role;

    private List<Rating> rating = new ArrayList<>();

}
