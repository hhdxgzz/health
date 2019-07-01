package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运营统计数据分析业务逻辑处理服务
 */
@Service(interfaceClass = ReportService.class) //发布服务
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() {

         Map<String,Object> result = new HashMap<>();

        //1.获取当前时间 2019-06-29
        try {
            String reportDate = DateUtils.parseDate2String(new Date(), "yyyy-MM-dd");
            // reportDate:null, 报表当前时间
            result.put("reportDate",reportDate);
            // todayNewMember :0, 今日新增会员数  select * from t_member where regTime > '2019-06-29'
            Integer todayNewMember = memberDao.findMemberCountAfterDate(reportDate);
            result.put("todayNewMember",todayNewMember);

            //获得本周一的日期
            String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            //获得本月第一天的日期
            String firstDay4ThisMonth =
                    DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
//总会员数
            Integer totalMember = memberDao.findMemberTotalCount();

            //本周新增会员数
            Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

            //本月新增会员数
            Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

            //今日预约数
            Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);

            //本周预约数
            Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);

            //本月预约数
            Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);

            //今日到诊数
            Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);

            //本周到诊数
            Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);

            //本月到诊数
            Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);

            result.put("totalMember",totalMember);
            result.put("thisWeekNewMember",thisWeekNewMember);
            result.put("thisMonthNewMember",thisMonthNewMember);
            result.put("todayOrderNumber",todayOrderNumber);
            result.put("thisWeekOrderNumber",thisWeekOrderNumber);
            result.put("thisMonthOrderNumber",thisMonthOrderNumber);
            result.put("todayVisitsNumber",todayVisitsNumber);
            result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // totalMember :0, 总会员数
        // thisWeekNewMember :0, 本周新增会员数
        // thisMonthNewMember :0, 本月新增会员数
        // todayOrderNumber :0, 今日预约数
        // todayVisitsNumber :0, 今日到诊数
        // thisWeekOrderNumber :0, 本周预约数
        // thisWeekVisitsNumber :0, 本周到诊数
        // thisMonthOrderNumber :0, 本月预约数
        // thisMonthVisitsNumber :0, 本月到诊数
        // hotSetmeal : List<Map> 热门套餐
        //套餐名称(setmeal)	预约数量(t_order)	占比(每个套餐预约总数/所有套餐的预约总数)	备注
        List<Map> hotSetmeal =setmealDao.findHotSetmeal();
        result.put("hotSetmeal",hotSetmeal);
        return result;
    }
}
