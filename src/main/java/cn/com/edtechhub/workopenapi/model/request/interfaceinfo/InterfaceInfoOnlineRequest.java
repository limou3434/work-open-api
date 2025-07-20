package cn.com.edtechhub.workopenapi.model.request.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
