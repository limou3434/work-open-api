package cn.com.edtechhub.workopenapi.service.impl;

import cn.com.edtechhub.workopenapi.common.exception.ErrorCode;
import cn.com.edtechhub.workopenapi.common.exception.ThrowUtils;
import cn.com.edtechhub.workopenapi.constants.UserConstant;
import cn.com.edtechhub.workopenapi.mapper.UserMapper;
import cn.com.edtechhub.workopenapi.model.entity.User;
import cn.com.edtechhub.workopenapi.service.UserService;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户服务实现
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值, 混淆密码
     */
    private static final String SALT = "edtechhub";

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 用户标识
     */
    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        ThrowUtils.throwIf("用户账号不能为空", StringUtils.isBlank(userAccount), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf("用户密码不能为空", StringUtils.isBlank(userPassword), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf("确认密码不能为空", StringUtils.isBlank(checkPassword), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf("用户密码与确认密码不一致", !userPassword.equals(checkPassword), ErrorCode.PARAMS_ERROR);

        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(this.encryptedPasswd(userPassword));
        user.setUserRole(UserConstant.DEFAULT_ROLE);
        boolean result = this.save(user);
        ThrowUtils.throwIf("操作失败", !result, ErrorCode.OPERATION_ERROR);

        return user.getId();
    }

    /**
     * 用户登入
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword) {
        User user = this.userValidation(userAccount, userPassword);
        StpUtil.login(user.getId()); // 允许登录(多设备)
        StpUtil.getSession().set(UserConstant.USER_LOGIN_STATE, user); // 把用户的信息存储到 Sa-Token 的会话中, 这样后续的用权限判断就不需要一直查询 SQL 才能得到, 缺点是更新权限的时候需要把用户踢下线否则会话无法更新
        return user;
    }

    /**
     * 用户登出
     *
     * @return 是否登出成功
     */
    @Override
    public Boolean userLogout() {
        StpUtil.logout(); // 默认所有设备都被登出
        return true;
    }

    /**
     * 获取当前登录用户标识
     *
     * @return 用户标识
     */
    @Override
    public Long getLoginUserId() {
        return Long.valueOf(StpUtil.getLoginId().toString());
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @Override
    public User getLoginUser() {
        SaSession session = StpUtil.getSessionByLoginId(this.getLoginUserId());
        ThrowUtils.throwIf("无法获取会话", session == null, ErrorCode.NOT_FOUND_ERROR);

        User user = null;
        if (session != null) {
            user = (User) session.get(UserConstant.USER_LOGIN_STATE);
        }
        ThrowUtils.throwIf("该用户尚未登录没有会话资源", user == null, ErrorCode.NOT_FOUND_ERROR);

        return user;
    }

    /**
     * 是否是管理员
     *
     * @return 是否是管理员
     */
    @Override
    public Boolean isAdminOfLoginUser() {
        User user = this.getLoginUser();
        if (user == null) {
            return false;
        }
        return UserConstant.ADMIN_ROLE.equals(user.getUserRole());
    }

    /**
     * 登录验证成功后获取用户信息
     *
     * @param account 账户
     * @param passwd  加密后的密码
     * @return 用户信息
     */
    public User userValidation(String account, String passwd) {
        // 参数检查
        ThrowUtils.throwIf("账户为空", StringUtils.isBlank(account), ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf("密码为空", StringUtils.isBlank(passwd), ErrorCode.PARAMS_ERROR);

        // 服务实现
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(User::getUserAccount, account)
                .eq(User::getUserPassword, this.encryptedPasswd(passwd));
        User user = this.getOne(lambdaQueryWrapper);
        log.debug("当前请求登录的用户 {}", user);
        return user;
    }

    /**
     * 获取加密后的密码
     *
     * @param passwd 密码
     * @return 加密后的密码
     */
    private String encryptedPasswd(String passwd) {
        ThrowUtils.throwIf("需要加密的密码不能为空", StringUtils.isAnyBlank(passwd), ErrorCode.PARAMS_ERROR);
        return DigestUtils.md5DigestAsHex((SALT + passwd).getBytes());
    }

}
