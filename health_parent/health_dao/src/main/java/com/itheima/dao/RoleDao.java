package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

public interface RoleDao {
    /**
     * select r.* from t_role r,t_user_role ur where r.id = ur.role_id and ur.user_id = 2
     * 根据用户id查询角色对象
     * @param userId
     * @return
     */
    Set<Role> findByUserId(Integer userId);
}
