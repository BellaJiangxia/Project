package com.winmine.mapper;

import com.winmine.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author 姜霞
 * @date 2020/10/27 16:35
 */
@Mapper
public interface UserMapper {
    //查数据
    public List<User> getUsers() throws Exception;

    //编辑用户
    public void updateUser(User user) throws Exception;

    //新增用户
    public void addUser(User user) throws Exception;

    //删除用户
    public void deleteUser(Integer id) throws Exception;

    // 根据用户名获取用户信息
    public User selectUserByLoginName(String username);

    // 用户登录
    public User loginUser(Map<String, Object> map);
}
