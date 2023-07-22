package com.org.controller;

import com.org.service.UserService;
import lombok.extern.slf4j.Slf4j;
import com.org.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * 获取用户列表
 * @author Leo
 * @date 18:48 2019/5/22
 **/
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * getUserList()
     * @author Leo
     * @date 18:49 2019/5/22
     * @param model
     * @return java.lang.String
     **/
    @RequestMapping("/list")
    public String toList(Model model){
        List<User> users=userService.getUserList();
        model.addAttribute("users",users);
        return "list";
    }
}