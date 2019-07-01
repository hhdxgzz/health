package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检查项接口层
 */
public interface CheckItemDao {

    /**
     * 新增检查项
     *
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     * 检查项分页查询
     *
     * @param queryString @Param("queryString")
     * @return
     */
    Page<CheckItem> selectByCondition(String queryString);

    /**
     * 根据检查项id删除检查项
     *
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 根据检查项id查询是否关联检查组
     *
     * @param id
     * @return
     */
    Integer findCountByCheckItemId(Integer id);

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
