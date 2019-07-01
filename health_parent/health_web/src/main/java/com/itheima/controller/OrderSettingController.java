package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.CheckGroupService;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 预约设置控制层管理
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    /**
     * 注入预约设置服务接口
     */
    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 预约设置批量导入数据
     * @return 返回结果 xxx
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result upload(@RequestParam MultipartFile excelFile) {
        try {
            //将excel处理完成后 调用service服务
            List<String[]> listStr = POIUtils.readExcel(excelFile);
            //为了服务层好处理 将listStr转为List<OrderSetting>
            List<OrderSetting> orderSettingList = new ArrayList<>();
            if(listStr!= null&& listStr.size()>0){
                //row:excel中每一行数据
                for (String[] row : listStr) {
                    OrderSetting orderSetting = new OrderSetting();
                    orderSetting.setOrderDate(new Date(row[0]));//设置日期
                    orderSetting.setNumber(Integer.parseInt(row[1]));
                    orderSettingList.add(orderSetting);
                }
            }
            //调用service服务
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 预约设置通过日历控件展示
     */
    @RequestMapping(value = "/getOrderSettingByMonth", method = RequestMethod.POST)
    public Result getOrderSettingByMonth(String date) {
        try {
            List<Map> listMap = orderSettingService.getOrderSettingByMonth(date);
            //[{key1:value1,key2:value2,....},{}]
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 基于日历实现预约设置
     */
    @RequestMapping(value = "/editNumberByDate", method = RequestMethod.POST)
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting) {
        try {
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
