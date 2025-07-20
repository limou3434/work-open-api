package cn.com.edtechhub.workopenapi.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口信息状态枚举
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Getter
public enum InterfaceInfoStatusEnum {

    OFFLINE("关闭", 0),

    ONLINE("上线", 1),

    ;

    /**
     * 描述
     */
    private final String description;

    /**
     * 码值
     */
    private final int value;

    /**
     * 内部构造
     *
     * @param description 描述
     * @param value       码值
     */
    InterfaceInfoStatusEnum(String description, int value) {
        this.description = description;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return 值列表
     */
    public static List<Integer> getValues() {
        return Arrays
                .stream(values())
                .map(item -> item.value)
                .collect(Collectors.toList())
                ;
    }

}
