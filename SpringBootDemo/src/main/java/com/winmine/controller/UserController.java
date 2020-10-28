package com.winmine.controller;

import com.winmine.entity.User;
import com.winmine.entity.UserInfo;
import com.winmine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 姜霞
 * @create 2020-10-23 14:00
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService service;

    // 显示出所有使用者
    @RequestMapping("/showList")
    @ResponseBody
    public List<User> showList() throws Exception {
        List<User> users = service.getUsers();
        System.out.println(users);
        return users;
    }

    @RequestMapping(value = "/login.html")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/login")
    public String login(User user) {
        User dbUser = new User(1, "zhangsan", "123456", "admin");
        if (dbUser.getPassword().equals(user.getPassword())) {
            UserInfo userInfo = new UserInfo(dbUser.getId(), dbUser.getUsername(), dbUser.getRole());
            HttpSession session = getRequest().getSession();
            session.setAttribute("user_info_in_the_session", userInfo);
            return "admin";
        }
        return "login";
    }

    @RequestMapping(value = "/userInfo")
    @ResponseBody
    public String userInfo() {
        HttpSession session = getRequest().getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute("user_info_in_the_session");
        return userInfo.toString();
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}
