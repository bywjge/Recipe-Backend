package com.recipe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User{

    @Id
    // 用GenerationType.AUTO的话Id会和Recipe重复，原因未知
    // https://stackoverflow.com/questions/14022374/the-differences-between-generatedvalue-strategies
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long userId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String role;
}
