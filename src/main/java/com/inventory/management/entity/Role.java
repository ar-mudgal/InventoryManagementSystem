package com.inventory.management.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import javax.persistence.*;

@Entity
@Table(name="role_table")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="admin")
    private String admin;

    @Column(name="user")
    private String user;


    @OneToOne(mappedBy = "role")
    private User userr;
}
