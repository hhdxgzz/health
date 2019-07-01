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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * 套餐管理-（移动端）
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    /**
     * 注入套餐服务接口
     */
    @Reference
    private SetmealService setmealService;
    /**
     * 移动端-查询套餐列表
     * @return
     */
    @RequestMapping(value = "/getSetmeal", method = RequestMethod.POST)
    public Result getSetmeal() {
        List<Setmeal> setmealList = setmealService.getSetmeal();
        return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmealList);
    }

    /**
     * 移动端-根据套餐id查询套餐详细信息（套餐数据+检查组数据+检查项数据）
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public Result findById(Integer id) {
        Setmeal setmeal = setmealService.findById(id);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

}
