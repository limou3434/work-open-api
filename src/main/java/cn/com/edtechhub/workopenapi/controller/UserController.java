package cn.com.edtechhub.workopenapi.controller;

import cn.com.edtechhub.workopenapi.common.exception.ErrorCode;
import cn.com.edtechhub.workopenapi.common.exception.ThrowUtils;
import cn.com.edtechhub.workopenapi.common.response.BaseResponse;
import cn.com.edtechhub.workopenapi.common.response.ResultUtils;
import cn.com.edtechhub.workopenapi.model.entity.User;
import cn.com.edtechhub.workopenapi.model.request.DeleteRequest;
import cn.com.edtechhub.workopenapi.model.request.user.*;
import cn.com.edtechhub.workopenapi.model.vo.UserVO;
import cn.com.edtechhub.workopenapi.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 注入用户服务依赖
     */
    @Resource
    private UserService userService;

    /// 增删查改 ///

    @Operation(summary = "创建用户")
    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", userAddRequest == null, ErrorCode.PARAMS_ERROR);
        assert userAddRequest != null;

        // 业务处理
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean result = userService.save(user);
        ThrowUtils.throwIf("操作失败", !result, ErrorCode.OPERATION_ERROR);

        // 返回结果
        return ResultUtils.success(user.getId());
    }

    @Operation(summary = "删除用户")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", deleteRequest == null, ErrorCode.PARAMS_ERROR);
        assert deleteRequest != null;

        ThrowUtils.throwIf("用户标识非法", deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);

        // 业务处理
        boolean result = userService.removeById(deleteRequest.getId());

        // 返回结果
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新用户")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", userUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        assert userUpdateRequest != null;

        ThrowUtils.throwIf("用户标识非法", userUpdateRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);

        // 业务处理
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);

        // 返回结果
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据标识获取用户信息")
    @GetMapping("/get")
    public BaseResponse<UserVO> getUserById(int id) {
        // 校验参数
        ThrowUtils.throwIf("用户标识非法", id <= 0, ErrorCode.PARAMS_ERROR);

        // 业务处理
        User user = userService.getById(id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);

        // 返回结果
        return ResultUtils.success(userVO);
    }

    @Operation(summary = "根据参数获取用户列表")
    @GetMapping("/list")
    public BaseResponse<List<UserVO>> listUser(UserQueryRequest userQueryRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        assert userQueryRequest != null;

        // 业务处理
        User userQuery = new User();
        BeanUtils.copyProperties(userQueryRequest, userQuery);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        List<User> userList = userService.list(queryWrapper);
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());

        // 返回结果
        return ResultUtils.success(userVOList);
    }

    @Operation(summary = "根据参数获取用户分页")
    @GetMapping("/list/page")
    public BaseResponse<Page<UserVO>> listUserByPage(UserQueryRequest userQueryRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        assert userQueryRequest != null;

        // 业务处理
        User userQuery = new User();

        BeanUtils.copyProperties(userQueryRequest, userQuery);
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>(userQuery);
        Page<User> userPage = userService.page(new Page<>(current, size), queryWrapper);
        Page<UserVO> userVOPage = new PageDTO<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).collect(Collectors.toList());
        userVOPage.setRecords(userVOList);

        // 返回结果
        return ResultUtils.success(userVOPage);
    }

    /// 功能接口 ///

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求体
     * @return 用户标识
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        assert userRegisterRequest != null;

        String userAccount = userRegisterRequest.getUserAccount();
        ThrowUtils.throwIf("用户账号不能为空", StringUtils.isBlank(userAccount), ErrorCode.PARAMS_ERROR);

        String userPassword = userRegisterRequest.getUserPassword();
        ThrowUtils.throwIf("用户密码不能为空", StringUtils.isBlank(userPassword), ErrorCode.PARAMS_ERROR);

        String checkPassword = userRegisterRequest.getCheckPassword();
        ThrowUtils.throwIf("确认密码不能为空", StringUtils.isBlank(checkPassword), ErrorCode.PARAMS_ERROR);

        // 业务处理
        long userId = userService.userRegister(userAccount, userPassword, checkPassword);

        // 返回结果
        return ResultUtils.success(userId);
    }

    /**
     * 用户登入
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        assert userLoginRequest != null;

        String userAccount = userLoginRequest.getUserAccount();
        ThrowUtils.throwIf("用户账号不能为空", StringUtils.isBlank(userAccount), ErrorCode.PARAMS_ERROR);

        String userPassword = userLoginRequest.getUserPassword();
        ThrowUtils.throwIf("用户密码不能为空", StringUtils.isBlank(userPassword), ErrorCode.PARAMS_ERROR);

        // 业务处理
        User user = userService.userLogin(userAccount, userPassword);

        // 返回结果
        return ResultUtils.success(user);
    }

    /**
     * 用户登出
     *
     * @return 是否登出成功
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {
        // 参数校验
        // ...

        // 业务处理
        Boolean result = userService.userLogout();

        // 返回结果
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser() {
        User user = userService.getLoginUser();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ResultUtils.success(userVO);
    }

}
