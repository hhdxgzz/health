package com.itheima.dao;

import com.itheima.pojo.User;

public interface UserDao {
    /**
     * select * from t_user where username = 'xiaoming'
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    User findByUsername(String username);
}
