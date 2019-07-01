package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预约设置接口服务
 */
public interface OrderSettingService {
    /**
     * 批量导入预约设置数据
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList);

    /**
     * 预约设置通过日历控件展示
     * @param date
     * @return
     */
    List<Map> getOrderSettingByMonth(String date);

    /**
     * 基于日历实现预约设置
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting);
}
