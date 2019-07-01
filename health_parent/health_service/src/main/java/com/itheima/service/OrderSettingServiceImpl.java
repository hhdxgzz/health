package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预约设置业务逻辑处理服务
 */
@Service(interfaceClass = OrderSettingService.class) //发布服务
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;


    /**
     * 批量导入预约设置数据
     * @param orderSettingList
     */
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        if(orderSettingList != null && orderSettingList.size()> 0){
            for (OrderSetting orderSetting : orderSettingList) {
                //1.根据预约日期查询数据是否存在
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(count>0){
                    //2.存在，根据预约日期修改预约人数
                    orderSettingDao.editNumberByOrderDate(orderSetting);//根据日期修改预约人数
                }
                else
                {
                    //3.不存在将Excel解析出来的数据保存数据库中
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 预约设置通过日历控件展示
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //1.处理date 2019-06=>startDate2019-06-01 endDate 2019-06-31
        String startDate = date+"-01";
        String endDate =date+"-31";
        //定义map
        Map<String,String> dateMap = new HashMap<>();
        //2.调用dao between startDate and endDate 得到list<orderSetting>
        dateMap.put("startDate",startDate);
        dateMap.put("endDate",endDate);
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(dateMap);
        //最终返回的结果对象
        List<Map> newRsMap = new ArrayList<>();
        //3.将List<orderSetting>转List<map>
        if(orderSettingList != null&&orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                Map<String,Object> rsMap = new HashMap<>();
                rsMap.put("date",orderSetting.getOrderDate().getDate());//获取当前几号
                rsMap.put("number",orderSetting.getNumber());//可预约的人数
                rsMap.put("reservations",orderSetting.getReservations());//已经预约的人数
                newRsMap.add(rsMap);
            }
        }
        return newRsMap;
    }

    /**
     * 基于日历实现预约设置
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //1.根据日期查询预约数据是否存在
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count>0){
            //2.存在，则修改
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }
        else
        {
            //3.不存在，则插入
            orderSettingDao.add(orderSetting);
        }
    }
}
