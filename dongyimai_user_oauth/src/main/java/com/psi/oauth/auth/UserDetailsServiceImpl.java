package com.psi.oauth.auth;

import com.psi.entity.Result;
import com.psi.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义SpringSecurity的权限控制实现类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    //远程调用UserFeign服务
    @Autowired
    private UserFeign userFeign;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Result<com.psi.user.pojo.User> user = null;

        try {
            //获取user
            user = userFeign.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("调用远程服务失败");
        }

        if (user == null) {
            return null;
        }

        //获取密码
        String password = user.getData().getPassword();

        //获取角色（从数据库中查）
        String permissions = "admin,root,user";

        //创建用户对象
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);

        User userDetail = new User(username, password, grantedAuthorities);

        return userDetail;
    }
}
