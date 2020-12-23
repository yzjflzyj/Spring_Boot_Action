package com.example.action;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.action.dao")
@SpringBootApplication
public class ActionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActionApplication.class, args);
    }

}
