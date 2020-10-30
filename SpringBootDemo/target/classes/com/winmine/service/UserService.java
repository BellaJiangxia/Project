package com.winmine.service;

import com.winmine.entity.User;

import java.util.List;

/**
 * @author 姜霞
 * @date 2020/10/27 17:03
 */
public interface UserService {
    public List<User> getUsers() throws Exception;

    public void updateUser(User user) throws Exception;

    public void addUser(User user) throws Exception;

    public void deleteUser(Integer id) throws Exception;

    public boolean checkLoginInfo(User user) throws Exception;
}
