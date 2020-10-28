package com.winmine.mapper;

import com.winmine.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 姜霞
 * @date 2020/10/27 16:35
 */
@Mapper
public interface UserMapper {
     // 1, 查数据
    public List<User> getUsers() throws Exception;

    // 2，编辑用户
    public void updateUser(User user) throws Exception;

    // 3, 新增用户
    public void addUser(User user) throws Exception;

    //4, 删除用户
    public void deleteUser(Integer id) throws Exception;
}
