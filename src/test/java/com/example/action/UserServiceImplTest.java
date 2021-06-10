package com.example.action;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.action.dao.UserDao;
import com.example.action.entity.User;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class UserServiceImplTest {

    @Resource
    UserDao userDao;

    @Test
    public void testSelect() {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", "yanjie");
        List<User> users = userDao.selectList(queryWrapper);
        System.out.println(users);
    }

    @Test
    public void testUpdate() {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper();
        List<Integer> list = new ArrayList<>();
        List ints = Arrays.asList(new int[]{1, 2, 3});
        userUpdateWrapper.eq("username", "yanjie").in("id", list).set("password", "pass123");
        User user = new User();
        userDao.update(user, userUpdateWrapper);

        System.out.println("好好");
    }
}
