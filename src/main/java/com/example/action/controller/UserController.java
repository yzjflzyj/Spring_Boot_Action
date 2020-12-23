package com.example.action.controller;



import com.example.action.entity.User;
import com.example.action.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
//    @GetMapping("selectOne")
//    public User selectOne(Integer id) {
//        return this.userService.queryById(id);
//    }

}