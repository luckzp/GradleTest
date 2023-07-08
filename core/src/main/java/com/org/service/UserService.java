package com.org.service;

import com.org.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;


/**
 * 用户信息service实现
 * @author Leo
 * @date 13:25 2019/5/22
 **/
@Service
public class UserService{
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    public List<User> getUserList() {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("beans.xml");
        logger.info("");
        List<User> userList=new ArrayList<>();
        //链式调用
        User userZs=new User().setId(1L).setName("张三").setAge(18);
        userList.add(userZs);

        return userList;
    }
}
