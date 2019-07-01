package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * 检查组接口层
 */
public interface CheckGroupService {
    /**
     * 新增检查组
     * @param checkitemIds
     * @param checkGroup
     */
    void add(Integer[] checkitemIds, CheckGroup checkGroup);

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 根据检查组id查询检查组信息
     */
    CheckGroup findById(Integer id);

    /**
     * 根据检查组id 查询中间表 检查项ids
     * @param id
     * @return
     */
    Integer[] findCheckItemIdsByCheckGroupId(Integer id);
    /**
     * 编辑检查组
     */
    void edit(Integer[] checkitemIds, CheckGroup checkGroup);

    /**
     * 查询所有检查组数据
     * @return
     */
    List<CheckGroup> findAll();
}
