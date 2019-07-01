package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 体检预约业务逻辑处理服务
 */
@Service(interfaceClass = OrderService.class) //发布服务
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;


    /**
     * 体检预约
     * 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
     * 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
     * 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
     * 4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
     * 5、预约成功，更新当日的已预约人数
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map)throws Exception {
        //需求：t_order预约表 member_id会员id orderDate:预约日期 orderType:预约类型 orderStauts:ORDERSTATUS_NO setmealId:套餐id
        System.out.println("体检预约服务被调用了");
        String telephone = (String)map.get("telephone");//手机号码
        String setmealId = (String)map.get("setmealId");//套餐id
        String orderType = (String)map.get("orderType");//预约类型
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = (String)map.get("orderDate");//页面选中的预约时间
        //将字符串日期转成date
        Date orderDate2 = DateUtils.parseString2Date(orderDate);
        //orderSetting为了后续业务逻辑使用 查询预约设置对象
        OrderSetting orderSetting  = orderSettingDao.findByOrderDate(orderDate2);
        if(orderSetting == null){
            //后台未设置预约日期
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已经预约人数
        if(reservations>=number){ //只要是已经预约人数>=可预约人数 则不可再预约
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3.查询是否是会员(根据手机号码）
        Member member = memberDao.findByTelephone(telephone);
        if(member != null){
            //根据会员id+预约日期+预约套餐id
            Order order = new Order(member.getId(),orderDate2,null,null,Integer.parseInt(setmealId));
            List<Order> listOrder = orderDao.findByCondition(order);
            if(listOrder != null && listOrder.size()>0){
                //重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }
        else
        {
            //会员不存在
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //将预约数据保存预约表中
        //预约日期orderDate2 //预约类型orderType  //预约状态 Order.ORDERSTATUS_NO //套餐id:setmealId
        Order orderDB = new Order(member.getId(),orderDate2,orderType,Order.ORDERSTATUS_NO,Integer.parseInt(setmealId));
        orderDao.add(orderDB);
        //返回order对象  根据orderid来查询 预约成功后显示数据

        //预约成功后 修改预约人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS,orderDB);
    }

    @Override
    public Map findById(Integer id) {
        return orderDao.findById4Detail(id);
    }
}
