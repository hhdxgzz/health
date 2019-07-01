package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * 检查组业务逻辑处理服务
 */
@Service(interfaceClass = CheckGroupService.class) //发布服务
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组
     * @param checkitemIds
     * @param checkGroup
     */
    @Override
    public void add(Integer[] checkitemIds, CheckGroup checkGroup) {
        //保存检查组表
        checkGroupDao.add(checkGroup);
        //得到检查组id
        Integer groupId = checkGroup.getId();
        //设置检查组和检查项中间表
        setCheckGroupAndCheckItem(groupId,checkitemIds);
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
        //设置分页参数
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> checkGroupPage = checkGroupDao.selectByCondition(queryString);
        return new PageResult(checkGroupPage.getTotal(), checkGroupPage.getResult());
    }
    /**
     * 根据检查组id查询检查组信息
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id 查询中间表 检查项ids
     * @param id
     * @return
     */
    @Override
    public Integer[] findCheckItemIdsByCheckGroupId(Integer id) {
        Integer[] ids = checkGroupDao.findCheckItemIdsByCheckGroupId(id);
        return ids;
    }

    @Override
    public void edit(Integer[] checkitemIds, CheckGroup checkGroup) {
        //1.修改检查组数据
        checkGroupDao.edit(checkGroup);
        //2.根据检查组id删除中间表数据  deleteAssociation
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //3.重新建立检查组和检查项关系 setCheckGroupAndCheckItem
        setCheckGroupAndCheckItem(checkGroup.getId(),checkitemIds);
    }
    /**
     * 查询所有检查组数据
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 设置检查组和检查项中间表
     * @param groupId
     * @param checkitemIds
     */
    private void setCheckGroupAndCheckItem(Integer groupId, Integer[] checkitemIds) {
        if(checkitemIds != null && checkitemIds.length> 0){
            Map<String,Integer> map = new HashMap();
            for (Integer checkitemId : checkitemIds) {
                map.put("checkitemId",checkitemId);
                map.put("groupid",groupId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
