package com.example.action.service.impl;
import com.example.action.dao.UserDao;
import com.example.action.entity.User;
import com.example.action.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2020-06-23 20:03:08
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;


    /**
     * 单条数据的新增或更新
     * @param user
     */
    @Override
    public void insertOrUpdateOne(User user){
        this.userDao.insertOrUpdateOne(user);
    }

    @Override
    public void insertOrUpdateMany(List<User> userList) {
        this.userDao.insertOrUpdateMany(userList);
    }
}