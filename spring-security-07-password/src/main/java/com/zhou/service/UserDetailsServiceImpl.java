package com.zhou.service;

import com.zhou.dao.UserMapper;
import com.zhou.entity.Role;
import com.zhou.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 实现 UserDetailsService 用于定制数据源, UserDetailsPasswordService 用来更新加密方式
 * @author zhoufuqi
 * @date 2023/2/14
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService {

    private UserMapper userMapper;

    @Autowired
    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 具体的验证方法
     *
     * @param username
     * @return org.springframework.security.core.userdetails.UserDetails
     * @author zhoufuqi
     * @date 2023/2/13 17:00
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 判断用户是否存在
        User user = userMapper.loadUserByUsername(username);
        if (Objects.isNull(user)) throw new UsernameNotFoundException("用户不存在");
        // 2. 设置权限
        List<Role> roles = userMapper.getRolesByUid(user.getId());
        user.setRoles(roles);
        // 3 返回用户
        return user;
    }

    /**
     * 具体更新密码的方法
     *
     * @param user        the user to modify the password for
     * @param newPassword the password to change to, encoded by the configured {@code PasswordEncoder}
     * @return
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Integer result = userMapper.updateUsernameByPassword(newPassword, user.getUsername());
        if (result == 1) {
            ((User)user).setPassword(newPassword);
        }
        return user;
    }
}