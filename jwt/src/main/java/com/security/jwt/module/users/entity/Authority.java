package com.security.jwt.module.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Authority {


    @Id
    @GeneratedValue
    private Long id;


    @Column(length = 50)
    private String authorityName;

    @ManyToOne
    @JoinColumn(name = "usersId")
    @JsonIgnore
    private Users users;
}
