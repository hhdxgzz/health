package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 检查项业务逻辑处理服务
 */
@Service(interfaceClass = CheckItemService.class) //发布服务
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //设置分页参数
        PageHelper.startPage(currentPage, pageSize);
        //select * from table where 条件1 =xx  PageHelper:通过拦截器 查询sql 拼接limit 进行分页
        //PageHelper提供的分页对象
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);
        return new PageResult(checkItemPage.getTotal(), checkItemPage.getResult());
    }

    /**
     * 根据检查项id删除检查项
     *
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        //删除之前 先检查是否和检查组 关联
        //关联 抛出异常 告知已经关联不能删除
        //select coun(*) from t_checkgroup_checkitem where checkitem_id = xxx
        Integer count = checkItemDao.findCountByCheckItemId(id);
        if (count > 0) {
            throw new RuntimeException("已经关联检查组不能删除");
        }
        //未关联
        checkItemDao.deleteById(id);
    }

    /**
     * 根据id查询检查项信息
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    /**
     * 更新检查项信息
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }
    /**
     * 查询所有检查项信息
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
