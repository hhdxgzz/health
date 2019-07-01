package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 移动端 用户快速登录
 */
@RestController
@RequestMapping("/login")
public class LoginController {


    /**
     * 注入体检预约服务接口
     */
    @Reference
    private MemberService memberService;


    @Autowired
    private JedisPool jedisPool;

    /**
     * 移动端-快速登录
     * 返回数据对象response：将cookie存入
     * @return
     */
    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Result check(HttpServletResponse response,@RequestBody Map map) {
        //判断验证码
        String telephone = (String)map.get("telephone");
        String validateCode = (String)map.get("validateCode");
        String redisCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if(StringUtils.isEmpty(validateCode) || StringUtils.isEmpty(redisCode) || !validateCode.equals(redisCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //根据手机号码查询是否是会员
        Member member = memberService.findByTelephone(telephone);
        //不是会员注册
        if(member == null){
            member = new Member();//除了手机号和注册日期，其余数据后续再完善
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }
        //不管是不是会员最终都直接存cookie
        Cookie cookie = new Cookie(Member.LOGIN_MEMBER_TELEPHONE,telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60*60*24*30);//有效期30天
        response.addCookie(cookie);//将cookie通过response对象返回
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
