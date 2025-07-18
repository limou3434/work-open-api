package cn.com.edtechhub.workopenapi.model.request.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Data
public class InterfaceInfoDeleteRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /// 序列化字段 ///
    private static final long serialVersionUID = 1L;

}