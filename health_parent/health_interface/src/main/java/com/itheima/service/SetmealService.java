package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐接口层
 */
public interface SetmealService {
    /**
     * 添加套餐
     * @param checkgroupIds
     * @param setmeal
     */
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

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
}
