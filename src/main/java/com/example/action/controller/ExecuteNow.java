package com.example.action.controller;


import com.example.action.entity.User;
import com.example.action.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ExecuteNow implements InitializingBean {

    @Resource
    private UserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("yanjie");
        user1.setSex("男");
        user1.setBirthday(new Date());
        user1.setAddress("深小圳");
        user1.setPassword("嘿嘿");

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("严志杰");
        user2.setSex("男");
        user2.setBirthday(new Date());
        user2.setAddress("深圳");
        user2.setPassword("呵呵");
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        //userService.insertOrUpdateOne(user1);
        userService.insertOrUpdateMany(userList);
    }
}
