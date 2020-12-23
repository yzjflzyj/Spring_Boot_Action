package com.example.action.dao;

import com.example.action.entity.User;

import java.util.List;

public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 单条数据的新增或者更新
     * @param user
     */
    void insertOrUpdateOne(User user);

    void insertOrUpdateMany(List<User> userList);
}