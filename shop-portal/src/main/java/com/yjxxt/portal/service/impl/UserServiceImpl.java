package com.yjxxt.portal.service.impl;

import com.yjxxt.portal.service.IUserService;
import com.yjxxt.rpc.service.ISmsService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class UserServiceImpl implements IUserService {

    @Reference(version = "1.0")
    private ISmsService smsService;


    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    public void saveUser() {

        /////////////////////////////
        /**
         * 用户记录添加
         */
        /////////////////////////////
        String phone="17624558348";
        amqpTemplate.convertAndSend("smsExchange", "registry.sms", phone);
    }
}
