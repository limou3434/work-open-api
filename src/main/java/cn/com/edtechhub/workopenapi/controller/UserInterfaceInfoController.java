package cn.com.edtechhub.workopenapi.controller;

import cn.com.edtechhub.workopenapi.common.exception.BusinessException;
import cn.com.edtechhub.workopenapi.common.exception.ErrorCode;
import cn.com.edtechhub.workopenapi.common.exception.ThrowUtils;
import cn.com.edtechhub.workopenapi.common.response.BaseResponse;
import cn.com.edtechhub.workopenapi.common.response.ResultUtils;
import cn.com.edtechhub.workopenapi.constants.CommonConstant;
import cn.com.edtechhub.workopenapi.model.entity.User;
import cn.com.edtechhub.workopenapi.model.entity.UserInterfaceInfo;
import cn.com.edtechhub.workopenapi.common.request.DeleteRequest;
import cn.com.edtechhub.workopenapi.model.request.userinterfaceinfo.UserInterfaceInfoAddRequest;
import cn.com.edtechhub.workopenapi.model.request.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import cn.com.edtechhub.workopenapi.model.request.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import cn.com.edtechhub.workopenapi.service.UserInterfaceInfoService;
import cn.com.edtechhub.workopenapi.service.UserService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户接口关联接口
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@RestController
@RequestMapping("/userInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    /**
     * 注入用户调用接口关联表服务依赖
     */
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 注入用户服务依赖
     */
    @Resource
    private UserService userService;

    @Operation(summary = "创建用户接口关联")
    @SaCheckLogin
    @PostMapping("/add")
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", userInterfaceInfoAddRequest == null, ErrorCode.PARAMS_ERROR);
        assert userInterfaceInfoAddRequest != null;

        // 业务处理
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);

        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);

        User loginUser = userService.getLoginUser();
        userInterfaceInfo.setUserId(loginUser.getId());
        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
        ThrowUtils.throwIf("操作失败", !result, ErrorCode.OPERATION_ERROR);

        // 返回结果
        long newUserInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    @Operation(summary = "删除用户接口关联")
    @SaCheckLogin
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        // 参数校验
        ThrowUtils.throwIf("请求体不能为空", deleteRequest == null, ErrorCode.PARAMS_ERROR);
        assert deleteRequest != null;

        ThrowUtils.throwIf("实体标识非法", deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);

        // 业务处理
        User user = userService.getLoginUser(); // 判断用户是否存在
        long id = deleteRequest.getId();
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf("用户接口关联不存在", oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        assert oldUserInterfaceInfo != null;

        ThrowUtils.throwIf("仅本人或管理员可删除", !oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdminOfLoginUser(), ErrorCode.NO_AUTH_ERROR);
        boolean b = userInterfaceInfoService.removeById(id);

        // 返回结果
        return ResultUtils.success(b);
    }

    @Operation(summary = "更新用户接口关联")
    @SaCheckLogin
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest) {
        // 参数校验
        ThrowUtils.throwIf("请求体不能为空", userInterfaceInfoUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        assert userInterfaceInfoUpdateRequest != null;

        ThrowUtils.throwIf("用户接口关联标识非法", userInterfaceInfoUpdateRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);

        // 业务处理
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);

        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);

        User user = userService.getLoginUser(); // 判断用户是否存在
        long id = userInterfaceInfoUpdateRequest.getId();
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf("用户接口关联不存在", oldUserInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        assert oldUserInterfaceInfo != null;

        ThrowUtils.throwIf("仅本人或管理员可修改", !oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdminOfLoginUser(), ErrorCode.NO_AUTH_ERROR);
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);

        // 返回结果
        return ResultUtils.success(result);
    }

    @Operation(summary = "根据标识获取用户接口关联")
    @SaCheckLogin
    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        // 参数校验
        ThrowUtils.throwIf("用户接口关联标识非法", id <= 0, ErrorCode.PARAMS_ERROR);

        // 业务处理
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);

        // 返回结果
        return ResultUtils.success(userInterfaceInfo);
    }

    @Operation(summary = "获取用户接口关联列表")
    @SaCheckLogin
    @GetMapping("/list")
    public BaseResponse<List<UserInterfaceInfo>> listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        // 参数校验
        ThrowUtils.throwIf("请求体不能为空", userInterfaceInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        assert userInterfaceInfoQueryRequest != null;

        // 业务处理
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);

        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoService.list(queryWrapper);

        // 返回结果
        return ResultUtils.success(userInterfaceInfoList);
    }

    @Operation(summary = "获取用户接口关联分页")
    @SaCheckLogin
    @GetMapping("/list/page")
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        // 参数校验
        ThrowUtils.throwIf("请求体不能为空", userInterfaceInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        assert userInterfaceInfoQueryRequest != null;

        // 业务处理
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);

        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();

        ThrowUtils.throwIf("不允许一次获取过多数据", size > 50, ErrorCode.PARAMS_ERROR);

        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);

        // 返回结果
        return ResultUtils.success(userInterfaceInfoPage);
    }

}
