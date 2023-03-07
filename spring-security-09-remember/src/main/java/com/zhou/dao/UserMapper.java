package com.zhou.dao;

import com.zhou.entity.Role;
import com.zhou.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhoufuqi
 * @date 2023/2/13
 */
@Mapper
public interface UserMapper {

    User loadUserByUsername(String username);

    List<Role> getRolesByUid(Long id);
}
