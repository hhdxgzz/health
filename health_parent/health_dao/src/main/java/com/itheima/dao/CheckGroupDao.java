package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    /**
     * 新增检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     * 设置检查组和检查项中间表
     */
    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     *分页查询
     * @param queryString
     * @return
     */
    Page<CheckGroup> selectByCondition(String queryString);
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
     * 修改检查组数据
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    /**
     * 根据检查组id删除中间表数据
     * @param id
     */
    void deleteAssociation(Integer id);
    /**
     * 查询所有检查组数据
     * @return
     */
    List<CheckGroup> findAll();
}
