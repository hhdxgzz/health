package com.itheima.security;

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
public class UserService implements UserDetailsService {
    //注入密码加密对象
    //encode:对明文进行加密
    //验证密码：matches 第一个参数：用户前台输入的明文密码  第二个参数：数据库中根据用户名查询出来的密码
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 造用户数据（后期改为查询数据库）
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    public  static  Map<String, com.itheima.pojo.User> map = new HashMap<>();
    public void init() {
        com.itheima.pojo.User user1 = new com.itheima.pojo.User();
        user1.setUsername("admin");
        user1.setPassword(encoder.encode("admin"));

        com.itheima.pojo.User user2 = new com.itheima.pojo.User();
        user2.setUsername("xiaoming");
        user2.setPassword(encoder.encode("1234"));

        map.put(user1.getUsername(),user1);
        map.put(user2.getUsername(),user2);
    }

    /**
     * 根据用户名家长用户对象
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //调用加载测试数据
        this.init();
        //根据用户名查询用户对象
        com.itheima.pojo.User user = map.get(username);
        if(user == null){
            return null;//框架就知道用户不存在 
        }
        String passwordDB = user.getPassword();
        //为当前用户授权的代码写死的，后续需要从数据库中查询
        List<GrantedAuthority>  grantedAuthorityList = new ArrayList<>();
        //ROLE_ADMIN:角色   单个权限：1个角色可以包含多个权限  ROLE_ADMIN包含add delete update xxx等
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//设置权限
        if(username.equals("admin")){
            grantedAuthorityList.add(new SimpleGrantedAuthority("add"));//设置权限
        }
        grantedAuthorityList.add(new SimpleGrantedAuthority("delete"));//设置权限
        //框架要求返回User对象
        //String username:用户名username String password:根据用户名查询数据库获取密码
        //  Collection<? extends GrantedAuthority> authorities：登录为用户授权
        //配置文件未加密测试
        //UserDetails userDetails = new User(username,"{noop}" +passwordDB,grantedAuthorityList);

        UserDetails userDetails = new User(username,passwordDB,grantedAuthorityList);
        return userDetails;//交给spring security框架
    }
}
