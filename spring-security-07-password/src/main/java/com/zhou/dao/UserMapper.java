package com.zhou.dao;

import com.zhou.entity.Role;
import com.zhou.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author zhoufuqi
 * @date 2023/2/13
 */
@Mapper
public interface UserMapper {
    public User loadUserByUsername(String username);

    public List<Role> getRolesByUid(Long id);

    public Integer updateUsernameByPassword(@Param("password") String password, @Param("username") String username);
}
