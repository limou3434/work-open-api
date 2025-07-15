package cn.com.edtechhub.workopenapi.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdRequest implements Serializable {

    /**
     * 接口 id
     */
    private long id;

    /// 序列化字段 ///
    private static final long serialVersionUID = 1L;

}
