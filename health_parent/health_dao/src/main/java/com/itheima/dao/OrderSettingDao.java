package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map; /**
 * 预约设置管理
 */
public interface OrderSettingDao {
    /**
     * 根据预约日期查询数据是否存在
     * @param orderDate
     * @return
     */
    long findCountByOrderDate(Date orderDate);

    /**
     * 存在，根据预约日期修改预约人数
     * @param orderSetting
     */
    void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 不存在将Excel解析出来的数据保存数据库中
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 根据月份获取预约设置数据
     * @param dateMap
     * @return
     */
    List<OrderSetting> getOrderSettingByMonth(Map<String, String> dateMap);

    /**
     * 根据预约时间查询预约设置对象
     * @param orderDate2
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate2);


    /**
     * 根据预约时间修改已经预约人数
     * @param orderSetting
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
