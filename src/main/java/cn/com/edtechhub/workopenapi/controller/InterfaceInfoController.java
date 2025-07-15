package cn.com.edtechhub.workopenapi.controller;

import cn.com.edtechhub.workoapiclisdk.client.ApiClient;
import cn.com.edtechhub.workoapiclisdk.model.User;
import cn.com.edtechhub.workopenapi.enums.InterfaceInfoStatusEnum;
import cn.com.edtechhub.workopenapi.exception.CodeBindMessageEnums;
import cn.com.edtechhub.workopenapi.exception.ThrowUtils;
import cn.com.edtechhub.workopenapi.model.dto.IdRequest;
import cn.com.edtechhub.workopenapi.model.entity.InterfaceInfo;
import cn.com.edtechhub.workopenapi.response.BaseResponse;
import cn.com.edtechhub.workopenapi.response.ResultUtils;
import cn.com.edtechhub.workopenapi.service.InterfaceInfoService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
     * 注入接口调用客户端依赖
     */
    @Resource
    private ApiClient apiClient;

    @Operation(summary = "👑发布接口")
    @PostMapping("/online")
    public BaseResponse<Boolean> onlineInterfaceInfo(@RequestBody IdRequest idRequest) {
        // 校验参数
        long id = idRequest.getId();
        ThrowUtils.throwIf("接口标识非法", id <= 0, CodeBindMessageEnums.PARAMS_ERROR);

        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf("该接口不存在", oldInterfaceInfo == null, CodeBindMessageEnums.NOT_FOUND_ERROR);

        // 业务处理
        // 1. 需要判断是否可以调用接口
        User user = new User();
        user.setUsername("test");
        String username = apiClient.getNameByPostWithRestful(user); // TODO: 这样相当于硬编码的, 后续需要改进
        ThrowUtils.throwIf("该接口无法被正常调用", StringUtils.isBlank(username), CodeBindMessageEnums.SYSTEM_ERROR);

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
        ThrowUtils.throwIf("接口标识非法", id <= 0, CodeBindMessageEnums.PARAMS_ERROR);

        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf("该接口不存在", oldInterfaceInfo == null, CodeBindMessageEnums.NOT_FOUND_ERROR);

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
