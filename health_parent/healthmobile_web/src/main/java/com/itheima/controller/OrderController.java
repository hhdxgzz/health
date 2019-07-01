package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.pojo.Setmeal;
import com.itheima.service.OrderService;
import com.itheima.service.SetmealService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;
/**
 * 提交预约管理-（移动端）
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    /**
     * 注入体检预约服务接口
     */
    @Reference
    private OrderService orderService;


    @Autowired
    private JedisPool jedisPool;

    /**
     * 移动端-体检预约
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Result submit(@RequestBody Map map) {
        /*  * {"setmealId":"3","sex":"1","idCard":"512501197203035172","name":"111",
                         * "telephone":"222222","validateCode":"333","orderDate":"2019-06-25"}
        */
        //1.校验验证码 redis跟页面
        String validateCode = (String)map.get("validateCode");
        String telephone = (String)map.get("telephone");
        String redisCode = (String)jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_ORDER);
        //提交代码前 取消注释
        if(StringUtils.isEmpty(validateCode) || StringUtils.isEmpty(redisCode) || !validateCode.equals(redisCode)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        //2.校验通过调用服务
        Result result  = null;
        try {
            //移动端标识
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            result = orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDER_FAIL);
        }
        //4.调用服务成功后，发送短信通知
        if(result.isFlag()){
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,(String)map.get("orderDate"));
            } catch (ClientException e) {
                e.printStackTrace();
                //需要有其它程序再处理
            }
        }
        return result;
    }

    /**
     * 预约成功跳转成功页面
     */
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public Result findById(Integer id) {
        Map map = orderService.findById(id);
        return new Result(true,MessageConstant.ORDER_SUCCESS,map);
    }

}
