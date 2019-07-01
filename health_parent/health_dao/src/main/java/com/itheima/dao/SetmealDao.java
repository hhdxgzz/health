package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    /**
     * 新增套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 设置套餐和检查组中间表关系
     */
    void setMealAndCheckGroup(Map<String, Integer> map);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Setmeal> selectByCondition(String queryString);

    /**
     * 移动端-查询套餐列表
     * @return
     */
    List<Setmeal> getSetmeal();

    /**
     * 移动端-根据套餐id查询套餐详细信息（套餐数据+检查组数据+检查项数据）
     * @return
     */
    Setmeal findById(Integer id);
    /**
     * 获取每个套餐预约总数 List<Map>
     * @return
     */
    List<Map> findSetmealCount();

    /**
     * 热门套餐
     * @return
     */
    List<Map> findHotSetmeal();
}
