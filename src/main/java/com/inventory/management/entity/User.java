package com.inventory.management.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role", referencedColumnName = "id")
//    private Set<Role> role = new HashSet<>();
    private Role role = new Role();
    @Transient
    private List<Rating> rating = new ArrayList<>();


//    @OneToOne(cascade = CascadeType.ALL )
//    @JoinColumn(insertable = true, updatable = true)
//    private SendPassword sendPasswordpass;

//    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
//    @JoinColumn(name="password", insertable = false, updatable = false)
//    private SendPassword sendPassword;

}
