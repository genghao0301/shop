package com.yjxxt.sso.service.impl;

import com.yjxxt.common.utils.Md5Util;
import com.yjxxt.common.utils.UUIDUtil;
import com.yjxxt.sso.mapper.AdminMapper;
import com.yjxxt.sso.pojo.Admin;
import com.yjxxt.sso.pojo.AdminExample;
import com.yjxxt.sso.service.ISSOService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(version = "1.0")
public class SSOServiceImpl implements ISSOService {

    @Resource
    private AdminMapper adminMapper;

    @Resource(name="redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String,Object> valueOperations;


    @Value("${user.ticket}")
    private String userTicket;

    @Override
    public String login(String userName, String password) {
        checkLoginParams(userName,password);
        AdminExample example=new AdminExample();
        example.createCriteria().andUserNameEqualTo(userName);
        // 查询用户记录是否存在
        List<Admin> userList= adminMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(userList)){
            throw  new RuntimeException("用户记录不存在!");
        }
        Admin admin = userList.get(0);
        // 比对密码
        password = Md5Util.getMd5WithSalt(password,admin.getEcSalt());
        if(!(password.equals(admin.getPassword()))){
            throw  new RuntimeException("用户密码错误!");
        }
        // 用户登录成功  生成用户票据
        String ticket = UUIDUtil.getUUID();
        // 将用户信息存入redis 缓存  30分钟有效
        valueOperations.set(userTicket+":"+ticket,admin,30, TimeUnit.MINUTES);
        return ticket;
    }



    private void checkLoginParams(String userName, String password) {
        if(StringUtils.isBlank(userName)){
            throw  new RuntimeException("用户名不能为空!");
        }

        if(StringUtils.isBlank(password)){
            throw new RuntimeException("密码不能为空!");
        }
    }

    /**
     * 验证票据的有效性
     * @param ticket 从cookie中获取
     * @return 返回用户信息
     */
    @Override
    public Admin validateTicket(String ticket) {
        if(StringUtils.isBlank(ticket)){
            //throw  new RuntimeException("票据非法!");
            System.out.println("票据非法!");
            return null;
        }
        String cacheKey = userTicket+":"+ticket;
        if(!redisTemplate.hasKey(cacheKey)){
            //throw  new RuntimeException("用户未登录或用户信息已过期");
            System.out.println("用户未登录或用户信息已过期!");
            return null;
        }

        return (Admin) valueOperations.get(cacheKey);
    }

    /**
     * 退出
     * @param ticket 从cookie中获取的
     */
    @Override
    public void logout(String ticket) {
        String cacheKey = userTicket+":"+ticket;
        if(redisTemplate.hasKey(cacheKey)){
            redisTemplate.delete(cacheKey);
        }
    }
}
