package cn.com.edtechhub.workopenapi.service;

import cn.com.edtechhub.workopenapi.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
* @author ljp
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-07-08 10:55:06
*/
public interface UserService extends IService<User> {

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

}
