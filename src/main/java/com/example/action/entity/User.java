package com.example.action.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * user
 * @author 
 */
@Data
public class User implements Serializable {
    private Integer id;

    private String username;

    private String sex;

    private Date birthday;

    private String address;

    private String password;

    private static final long serialVersionUID = 1L;
}