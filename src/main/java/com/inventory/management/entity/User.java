package com.inventory.management.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name="user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer userId;

    @Column(name="user_name")
    private String name;

    @Column(name="gender")
    private String gender;


    @Column(name="mobile")
    private String mobile;


    @Column(name="email")
    private String email;


    @Column(name="password")
    private String password;


    @Column(name="address")
    private String address;


    @Column(name="pin_code")
    private String pincode;


    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER,mappedBy = "user")
//    private Set<Role> role = new HashSet<>();
    private Role role = new Role();

    @Transient
    private List<Rating> rating = new ArrayList<>();

}
