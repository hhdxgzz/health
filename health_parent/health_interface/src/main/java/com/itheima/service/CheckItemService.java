package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 检查项接口层
 */
public interface CheckItemService {
    /**
     * 添加检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页
     * @param currentPage  当前页码
     * @param pageSize 每页显示记录苏
     * @param queryString 查询条件
     * @return 返回分页对象
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据检查项id删除检查项
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据id查询检查项信息
     */
    CheckItem findById(Integer id);
    /**
     * 更新检查项信息
     */
    void edit(CheckItem checkItem);
    /**
     * 查询所有检查项信息
     */
    List<CheckItem> findAll();
}
