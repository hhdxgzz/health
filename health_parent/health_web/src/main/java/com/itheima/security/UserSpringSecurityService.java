package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 自定义类 实现根据用户名查询用户对象（为了后续做认证用户） 和授权
 */
@Component
public class UserSpringSecurityService implements UserDetailsService {
    //注入密码加密对象
    //encode:对明文进行加密
    //验证密码：matches 第一个参数：用户前台输入的明文密码  第二个参数：数据库中根据用户名查询出来的密码
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Reference
    private UserService userService;

    /**
     * 根据用户名家长用户对象
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名 查询用户表
        com.itheima.pojo.User user = userService.findByUsername(username);
        if(user == null){
            //2.用户不存在 则直接返回null
            return  null;
        }
        //定义一个list集合用于存放权限
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        Set<Role> roles = user.getRoles();//根据用户获取角色
        if(roles != null){
            //循环Role集合
            for (Role role : roles) {
                //将角色关键字 添加到list
                grantedAuthorityList.add(new SimpleGrantedAuthority(role.getKeyword()));
                Set<Permission> permissions = role.getPermissions();        
                if(permissions != null){
                    for (Permission permission : permissions) {
                        grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }
        UserDetails userDetails = new User(username,user.getPassword(),grantedAuthorityList);
        return userDetails;//交给spring security框架
    }
}
