package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    /**
     * select p.* from t_permission p,t_role_permission rp where p.id = rp.permission_id and rp.role_id = 2
     * 根据角色id查询权限对象
     * @param roleId
     * @return
     */
    Set<Permission> findByRoleId(Integer roleId);
}
