package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

/**
 * 体检预约接口服务
 */
public interface OrderService {

    /**
     * 体检预约（移动端 && 后台）
     * @param map
     * @return
     */
    Result submitOrder(Map map)throws Exception;

    /**
     * 预约成功对象
     * @param id
     * @return
     */
    Map findById(Integer id);
}
