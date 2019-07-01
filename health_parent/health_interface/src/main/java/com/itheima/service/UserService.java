package com.itheima.service;

import com.itheima.pojo.User;

/**
 * 权限管理服务接口
 */
public interface UserService {
    /**
     * 权限功能 根据用户查询用户对象
     * 根据用户id查询角色对象
     * 根据角色id查询权限对象
     * @param username
     * @return
     */
    User findByUsername(String username);
}
