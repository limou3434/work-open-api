package cn.com.edtechhub.workopenapi.service.impl;

import cn.com.edtechhub.workopenapi.common.exception.ErrorCode;
import cn.com.edtechhub.workopenapi.common.exception.ThrowUtils;
import cn.com.edtechhub.workopenapi.mapper.InterfaceInfoMapper;
import cn.com.edtechhub.workopenapi.model.entity.InterfaceInfo;
import cn.com.edtechhub.workopenapi.service.InterfaceInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author ljp
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2025-07-06 17:27:08
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        ThrowUtils.throwIf("接口信息不能为空", interfaceInfo == null, ErrorCode.PARAMS_ERROR);
        assert interfaceInfo != null;

        // 需要检查一些参数
        String name = interfaceInfo.getName();
        if (add) { // 添加
            ThrowUtils.throwIf("接口信息不能为空", StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
        } else { // 更新
            ThrowUtils.throwIf("名称过长", StringUtils.isNotBlank(name) && name.length() > 50, ErrorCode.PARAMS_ERROR);
        }
    }

}




