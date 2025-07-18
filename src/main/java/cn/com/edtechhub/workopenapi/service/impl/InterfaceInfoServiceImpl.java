package cn.com.edtechhub.workopenapi.service.impl;

import cn.com.edtechhub.workopenapi.exception.BusinessException;
import cn.com.edtechhub.workopenapi.exception.CodeBindMessageEnums;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.com.edtechhub.workopenapi.model.entity.InterfaceInfo;
import cn.com.edtechhub.workopenapi.service.InterfaceInfoService;
import cn.com.edtechhub.workopenapi.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* @author ljp
* @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
* @createDate 2025-07-06 17:27:08
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(CodeBindMessageEnums.PARAMS_ERROR, "");
        }
        String name = interfaceInfo.getName();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(CodeBindMessageEnums.PARAMS_ERROR, "");
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(CodeBindMessageEnums.PARAMS_ERROR, "名称过长");
        }
    }

}




