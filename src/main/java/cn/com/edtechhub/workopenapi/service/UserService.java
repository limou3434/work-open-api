package cn.com.edtechhub.workopenapi.service;

import cn.com.edtechhub.workopenapi.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登入
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 用户信息
     */
    User userLogin(String userAccount, String userPassword);

    /**
     * 用户登出
     *
     * @return 是否登出成功
     */
    Boolean userLogout();

    /**
     * 获取当前登录用户标识
     *
     * @return 用户标识
     */
    Long getLoginUserId();

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    User getLoginUser();

    /**
     * 判断当前登陆用户是否为管理员
     *
     * @return 是否是管理员
     */
    Boolean isAdminOfLoginUser();

}
