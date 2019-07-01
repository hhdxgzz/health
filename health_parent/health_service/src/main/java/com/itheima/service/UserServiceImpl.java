package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 权限管理业务逻辑处理服务
 */
@Service(interfaceClass = UserService.class) //发布服务
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findByUsername(String username) {
        //1.根据用户名 查询用户表
        User user = userDao.findByUsername(username);
        if(user == null){
            //2.用户不存在 则直接返回null
            return null;
        }
        //3. 根据用户id查询角色对象
        Integer userId = user.getId();

        Set<Role> roles  = roleDao.findByUserId(userId);

        if(roles != null && roles.size()>0){
            //将角色设置用户对象中
            user.setRoles(roles);
            //4.根据角色id查询权限对象
            for (Role role : roles) {
                Integer roleId = role.getId();//角色id
                Set<Permission> permissions  = permissionDao.findByRoleId(roleId);
                if(permissions != null && permissions.size()>0){
                    role.setPermissions(permissions);
                }
            }
        }
        return user;
    }
}
