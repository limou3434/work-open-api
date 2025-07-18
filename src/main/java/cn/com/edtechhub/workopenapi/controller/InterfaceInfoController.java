package cn.com.edtechhub.workopenapi.controller;

import cn.com.edtechhub.workoapiclisdk.client.ApiClient;
import cn.com.edtechhub.workopenapi.common.exception.BusinessException;
import cn.com.edtechhub.workopenapi.common.exception.ErrorCode;
import cn.com.edtechhub.workopenapi.common.exception.ThrowUtils;
import cn.com.edtechhub.workopenapi.common.response.BaseResponse;
import cn.com.edtechhub.workopenapi.common.response.ResultUtils;
import cn.com.edtechhub.workopenapi.constants.CommonConstant;
import cn.com.edtechhub.workopenapi.enums.InterfaceInfoStatusEnum;
import cn.com.edtechhub.workopenapi.model.dto.IdRequest;
import cn.com.edtechhub.workopenapi.model.entity.InterfaceInfo;
import cn.com.edtechhub.workopenapi.model.entity.User;
import cn.com.edtechhub.workopenapi.model.request.DeleteRequest;
import cn.com.edtechhub.workopenapi.model.request.interfaceinfo.InterfaceInfoAddRequest;
import cn.com.edtechhub.workopenapi.model.request.interfaceinfo.InterfaceInfoQueryRequest;
import cn.com.edtechhub.workopenapi.model.request.interfaceinfo.InterfaceInfoUpdateRequest;
import cn.com.edtechhub.workopenapi.service.InterfaceInfoService;
import cn.com.edtechhub.workopenapi.service.UserService;
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
 * 接口管理
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    /**
     * 注入接口信息服务依赖
     */
    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 注入用户服务依赖
     */
    @Resource
    private UserService userService;

    /**
     * 注入接口调用客户端依赖
     */
    @Resource
    private ApiClient apiClient;

    /// 增删查改 ///

    @Operation(summary = "创建接口信息")
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
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "");
        }
        long newInterfaceInfoId = interfaceInfo.getId();

        // 返回结果
        return ResultUtils.success(newInterfaceInfoId);
    }

    @Operation(summary = "删除接口信息")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest) {
        return ResultUtils.notyet();
    }

    @Operation(summary = "更新接口信息")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "");
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        User user = userService.getLoginUser();
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "");
        }
        // 仅本人或管理员可修改
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "");
        }
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @Operation(summary = "获取接口信息")
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "");
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    @Operation(summary = "👑获取接口信息列表")
    @GetMapping("/list")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }

    @Operation(summary = "👑获取接口信息分页")
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "");
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "");
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }

    /// 功能接口 ///

    @Operation(summary = "👑发布接口")
    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        // 校验参数
        long id = idRequest.getId();
        ThrowUtils.throwIf("接口标识非法", id <= 0, ErrorCode.PARAMS_ERROR);

        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf("该接口不存在", oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);

        // 业务处理
        // 1. 需要判断是否可以调用接口
        User user = new User();
//        user.setUsername("test");
//        String username = apiClient.getNameByPostWithRestful(user); // TODO: 这样相当于硬编码的, 后续需要改进
//        ThrowUtils.throwIf("该接口无法被正常调用", StringUtils.isBlank(username), ErrorCode.SYSTEM_ERROR);

        // 2. 设置接口状态, 此时状态标识为上线
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);

        // 返回结果
        return ResultUtils.success(result);
    }

    @Operation(summary = "👑下线接口")
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        // 校验参数
        long id = idRequest.getId();
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
