package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 套餐业务逻辑处理服务
 */
@Service(interfaceClass = SetmealService.class) //发布服务
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 新增套餐
     * @param checkgroupIds
     * @param setmeal
     */
    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        //保存套餐表t_setmeal
        setmealDao.add(setmeal);
        //得到套餐id
        Integer setmealId = setmeal.getId();
        //设置套餐和检查组中间表关系
        setMealAndCheckGroup(setmealId,checkgroupIds);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> setmealPage = setmealDao.selectByCondition(queryString);
        return new PageResult(setmealPage.getTotal(), setmealPage.getResult());
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    /**
     * 移动端-根据套餐id查询套餐详细信息（套餐数据+检查组数据+检查项数据）
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }
    /**
     * 获取每个套餐预约总数 List<Map>
     * @return
     */
    @Override
    public List<Map> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    /**
     * 设置套餐表和检查组表关系
     */
    public void setMealAndCheckGroup(Integer setmealId,Integer[] checkgroupIds){
        if(checkgroupIds != null && checkgroupIds.length> 0){
            Map<String,Integer> map = new HashMap();
            for (Integer checkgroupId : checkgroupIds) {
                map.put("checkgroupId",checkgroupId);
                map.put("setmealId",setmealId);
                setmealDao.setMealAndCheckGroup(map);
            }
        }
    }
}
