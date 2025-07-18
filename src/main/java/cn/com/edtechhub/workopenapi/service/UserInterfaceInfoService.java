package cn.com.edtechhub.workopenapi.service;

import cn.com.edtechhub.workopenapi.model.entity.UserInterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ljp
* @description 针对表【user_interface_info(用户调用接口关联表)】的数据库操作Service
* @createDate 2025-07-08 10:55:06
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 校验用户调用接口关联表
     * @param userInterfaceInfo 用户调用接口关联表
     * @param add 是否添加
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId 接口 id
     * @param userId 用户 id
     * @return 是否调用成功
     */
    boolean invokeCount(long interfaceInfoId, long userId);

}
