package com.zhou.service;

import com.zhou.dao.UserMapper;
import com.zhou.entity.Role;
import com.zhou.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author zhoufuqi
 * @date 2023/2/14
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

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

}
