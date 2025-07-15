package cn.com.edtechhub.workopenapi.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户脱敏
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /// 序列化字段 ///
    private static final long serialVersionUID = 1L;

}