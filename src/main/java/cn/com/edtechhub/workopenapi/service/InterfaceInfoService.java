package cn.com.edtechhub.workopenapi.service;

import cn.com.edtechhub.workopenapi.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ljp
* @description 针对表【interface_info(接口信息表)】的数据库操作Service
* @createDate 2025-07-06 17:27:08
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
