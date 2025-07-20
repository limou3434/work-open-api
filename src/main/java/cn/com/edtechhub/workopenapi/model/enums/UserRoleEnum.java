package cn.com.edtechhub.workopenapi.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Getter
public enum UserRoleEnum {

    USER("用户", ""), // TODO: 使用 1/0 来标识更加节省空间

    ADMIN("管理", ""),

    ;

    /**
     * 描述
     */
    private final String description;

    /**
     * 码值
     */
    private final String value;

    /**
     * 内部构造
     *
     * @param description 描述
     * @param value       码值
     */
    UserRoleEnum(String description, String value) {
        this.description = description;
        this.value = value;
    }

    /**
     * 根据角色码值获取角色枚举
     */
    public static UserRoleEnum getEnums(String code) { // TODO: 等待改正放回值为枚举
        if (ObjUtil.isEmpty(code)) {
            return null;
        }
        for (UserRoleEnum role : UserRoleEnum.values()) {
            if (Objects.equals(role.getValue(), code)) {
                return role;
            }
        }
        return null;
    }

    /**
     * 获取值列表
     *
     * @return 值列表
     */
    public static List<String> getValues() {
        return Arrays
                .stream(values())
                .map(item -> item.value)
                .collect(Collectors.toList())
                ;
    }

}
