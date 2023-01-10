package com.inventory.management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="password_table")
public class SendPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

//    @OneToOne(mappedBy = "sendPassword")
//    private User user;

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sendPassword")
//    private User user;
}
