package com.example.action.service;


import com.example.action.entity.User;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2020-06-23 20:03:07
 */
public interface UserService {

    void insertOrUpdateOne(User user);

    void insertOrUpdateMany(List<User> userList);
}