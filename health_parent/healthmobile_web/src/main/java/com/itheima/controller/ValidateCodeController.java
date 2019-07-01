package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * 提交预约发送验证码-（移动端）
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    //定义一个日志对象
    private Logger logger = LoggerFactory.getLogger(ValidateCodeController.class);

    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约
     * @param telephone
     * @return
     */
    @RequestMapping(value = "/send4Order", method = RequestMethod.POST)
    public Result send4Order(String telephone) {
        //在发送验证码之前 根据手机号码到redis中查询 验证码是否发送
        //如果已经发送过验证码，返回验证码操作过于频繁

        //1.生成验证码 调用工具类
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        //2.发送验证码 调用阿里云短信接口
        try {

            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());

            //3.存入redis中 后续体检预约前校验验证码是否正确  有效期为5分钟
            System.out.println("手机号码：：：：："+telephone+"::::::"+code.toString());
            //key:手机号+业务唯一标识  value:code  seconds:秒
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }


    /**
     * 登录验证码生成 发送短信给用户
     * @param telephone
     * @return
     */
    @RequestMapping(value = "/send4Login", method = RequestMethod.POST)
    public Result send4Login(String telephone) {
        //在发送验证码之前 根据手机号码到redis中查询 验证码是否发送
        //如果已经发送过验证码，返回验证码操作过于频繁

        //1.生成验证码 调用工具类
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        //2.发送验证码 调用阿里云短信接口

        //trace debug（开发阶段可以设置为此日志级别） info（生成环境可以设置为此级别） warn  error(优先级别最高)
        try {
            logger.info("info::::"+telephone); //输入 和 输出

            logger.warn("warn::::");//业务中警告
            //logger.trace("trace::::");
            logger.debug("debug::::");//开发阶段
            //System.out.println("发送短信验证码成功了。。。。");
            if(false) {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
            }
            //3.存入redis中 后续体检预约前校验验证码是否正确  有效期为5分钟
           // System.out.println("手机号码：：：：："+telephone+"::::::"+code.toString());
            //key:手机号+业务唯一标识  value:code  seconds:秒
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());

            logger.info("结果：：："+telephone+"验证码：：：："+code.toString());
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            logger.error(e.getMessage());
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
