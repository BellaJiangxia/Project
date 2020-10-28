package com.winmine.service.impl;

import com.winmine.entity.User;
import com.winmine.mapper.UserMapper;
import com.winmine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 姜霞
 * @date 2020/10/27 17:04
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getUsers() throws Exception {
        return userMapper.getUsers();
    }

    @Override
    public void updateUser(User user) throws Exception {
        userMapper.updateUser(user);
    }

    @Override
    public void addUser(User user) throws Exception {
        userMapper.addUser(user);
    }

    @Override
    public void deleteUser(Integer id) throws Exception {
        userMapper.deleteUser(id);
    }
}
