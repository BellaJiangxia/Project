package com.winmine.service.impl;

import com.winmine.common.StringUtil;
import com.winmine.entity.User;
import com.winmine.mapper.UserMapper;
import com.winmine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public boolean checkLoginInfo(User user) throws Exception {
        boolean isSuccess = false;
        Map<String, Object> prms = new HashMap<String, Object>();
        prms.put("login_name", user.getUserName());
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String strNewPwd = StringUtil.toHexString((md5.digest(user.getPassWord().getBytes("utf-8"))));
        prms.put("pwd", strNewPwd);

        User dbuser = userMapper.loginUser(prms);
        if (dbuser == null)
            throw new Exception("用户名或密码错误");
        isSuccess = true;
        return isSuccess;
    }
}
