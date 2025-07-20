package cn.com.edtechhub.workopenapi.model.request.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口 id 请求
 */
@Data
public class InterfaceInfoOnlineRequest implements Serializable {

    /**
     * 接口 id
     */
    private long id;

    /// 序列化字段 ///
    private static final long serialVersionUID = 1L;

}
