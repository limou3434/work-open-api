package cn.com.edtechhub.workopenapi.controller;

import cn.com.edtechhub.workoapiclisdk.client.ApiClient;
import cn.com.edtechhub.workopenapi.common.exception.ErrorCode;
import cn.com.edtechhub.workopenapi.common.exception.ThrowUtils;
import cn.com.edtechhub.workopenapi.common.request.DeleteRequest;
import cn.com.edtechhub.workopenapi.common.response.BaseResponse;
import cn.com.edtechhub.workopenapi.common.response.ResultUtils;
import cn.com.edtechhub.workopenapi.constants.CommonConstant;
import cn.com.edtechhub.workopenapi.model.entity.InterfaceInfo;
import cn.com.edtechhub.workopenapi.model.entity.User;
import cn.com.edtechhub.workopenapi.model.enums.InterfaceInfoStatusEnum;
import cn.com.edtechhub.workopenapi.model.request.interfaceinfo.InterfaceInfoAddRequest;
import cn.com.edtechhub.workopenapi.model.request.interfaceinfo.InterfaceInfoOnlineRequest;
import cn.com.edtechhub.workopenapi.model.request.interfaceinfo.InterfaceInfoQueryRequest;
import cn.com.edtechhub.workopenapi.model.request.interfaceinfo.InterfaceInfoUpdateRequest;
import cn.com.edtechhub.workopenapi.service.InterfaceInfoService;
import cn.com.edtechhub.workopenapi.service.UserService;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 接口接口
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    /**
     * 注入 api 客户端
     */
    @Resource
    private ApiClient apiClient;

    /**
     * 注入用户服务依赖
     */
    @Resource
    private UserService userService;

    /**
     * 注入接口信息服务依赖
     */
    @Resource
    private InterfaceInfoService interfaceInfoService;

    /// 增删查改 ///

    @Operation(summary = "创建接口信息")
    @SaCheckLogin
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求参数不能为空", interfaceInfoAddRequest == null, ErrorCode.PARAMS_ERROR);
        assert interfaceInfoAddRequest != null;

        // 业务处理
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);

        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);

        User loginUser = userService.getLoginUser();
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf("操作失败", !result, ErrorCode.OPERATION_ERROR);

        // 返回结果
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    @Operation(summary = "删除接口信息")
    @SaCheckLogin
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest) { // TODO: 删除接口可能需要修改仅本人和管理可以使用, 最好写一个拓展注解
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", deleteRequest == null, ErrorCode.PARAMS_ERROR);
        assert deleteRequest != null;

        ThrowUtils.throwIf("实体标识非法", deleteRequest.getId() <= 0, ErrorCode.PARAMS_ERROR);

        // 业务处理
        boolean result = interfaceInfoService.removeById(deleteRequest.getId());

        // 返回结果
        return ResultUtils.success(result);
    }

    @Operation(summary = "更新接口信息")
    @SaCheckLogin
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", interfaceInfoUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        assert interfaceInfoUpdateRequest != null;

        long id = interfaceInfoUpdateRequest.getId();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id); // 判断接口是否存在
        ThrowUtils.throwIf("该接口不存在", oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        assert oldInterfaceInfo != null;

        User loginUser = userService.getLoginUser();
        ThrowUtils.throwIf("仅本人或管理员可修改, 您无权修改该接口", !oldInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdminOfLoginUser(), ErrorCode.NO_AUTH_ERROR);

        // 业务处理
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);

        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);

        boolean result = interfaceInfoService.updateById(interfaceInfo);

        // 返回结果
        return ResultUtils.success(result);
    }

    @Operation(summary = "获取接口信息")
    @SaCheckLogin
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        // 校验参数
        ThrowUtils.throwIf("接口标识非法", id <= 0, ErrorCode.PARAMS_ERROR);

        // 业务处理
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);

        // 返回结果
        return ResultUtils.success(interfaceInfo);
    }

    @Operation(summary = "获取接口信息列表")
    @SaCheckLogin
    @SaCheckRole("admin")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", interfaceInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        assert interfaceInfoQueryRequest != null;

        // 业务处理
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);

        // 返回结果
        return ResultUtils.success(interfaceInfoList);
    }

    @Operation(summary = "👑获取接口信息分页")
    @SaCheckLogin
    @SaCheckRole("admin")
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        // 校验参数
        ThrowUtils.throwIf("请求体不能为空", interfaceInfoQueryRequest == null, ErrorCode.PARAMS_ERROR);
        assert interfaceInfoQueryRequest != null;

        // 业务处理
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);

        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();

        String description = interfaceInfoQuery.getDescription();

        interfaceInfoQuery.setDescription(null); // description 需支持模糊搜索

        ThrowUtils.throwIf("爬虫请求非法", size > 50, ErrorCode.PARAMS_ERROR); // 限制爬虫

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);

        // 返回结果
        return ResultUtils.success(interfaceInfoPage);
    }

    /// 功能接口 ///

    @Operation(summary = "👑发布接口")
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody InterfaceInfoOnlineRequest interfaceInfoOnlineRequest) {
        // 校验参数
        long id = interfaceInfoOnlineRequest.getId();
        ThrowUtils.throwIf("接口标识非法", id <= 0, ErrorCode.PARAMS_ERROR);

        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf("该接口不存在", oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        // 业务处理
        // 1. 需要判断是否可以调用接口
        cn.com.edtechhub.workoapiclisdk.model.User user = new cn.com.edtechhub.workoapiclisdk.model.User();
        user.setUsername("test");
        String username = apiClient.getNameByPostWithRestful(user); // TODO: 这样相当于硬编码的, 后续需要改进
        ThrowUtils.throwIf("该接口无法被正常调用", StringUtils.isBlank(username), ErrorCode.SYSTEM_ERROR);

        // 2. 设置接口状态, 此时状态标识为上线
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);

        // 返回结果
        return ResultUtils.success(result);
    }

    @Operation(summary = "👑下线接口")
    @SaCheckLogin
    @SaCheckRole("admin")
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody InterfaceInfoOnlineRequest interfaceInfoOnlineRequest) {
        // 校验参数
        long id = interfaceInfoOnlineRequest.getId();
        ThrowUtils.throwIf("接口标识非法", id <= 0, ErrorCode.PARAMS_ERROR);

        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf("该接口不存在", oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        // 业务处理
        // 1. 无需判断是否可以调用接口

        // 2. 设置接口状态, 此时状态标识为上线
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);

        // 返回结果
        return ResultUtils.success(result);
    }

}
