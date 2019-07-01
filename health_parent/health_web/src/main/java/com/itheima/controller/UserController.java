package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * 用户控制层管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取用户名
     * @return
     */
    @RequestMapping(value = "/getUsername", method = RequestMethod.GET)
    public Result getUsername() {
        //通过spring security框架中提供SecurityContextHolder来获取用户对象
        //SecurityContext 安全容器对象
        // 通过容器安全对象 获取 Authentication认证 ,
        // 最终通过Authentication认证获取用户对象principal
        User principal =(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();//获取用户名
        return new Result(true,MessageConstant.GET_USERNAME_SUCCESS,username);
    }
}
