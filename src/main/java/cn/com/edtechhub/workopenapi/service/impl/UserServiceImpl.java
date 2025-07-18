package cn.com.edtechhub.workopenapi.service.impl;

import cn.com.edtechhub.workopenapi.mapper.UserMapper;
import cn.com.edtechhub.workopenapi.model.entity.User;
import cn.com.edtechhub.workopenapi.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ljp
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2025-07-08 10:55:06
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 注入用户映射依赖
     */
    @Resource
    private UserMapper userMapper;

    /**
     * 盐值, 混淆密码
     */
    private static final String SALT = "edtechhub";

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        return null;
    }

    @Override
    public User userLogin(String userAccount, String userPassword) {
        return null;
    }

    @Override
    public Boolean userLogout() {
        return null;
    }

    @Override
    public Long getLoginUserId() {
        return null;
    }

    @Override
    public User getLoginUser() {
        return null;
    }

    @Override
    public Boolean isAdmin() {
        return true;
    }

}
