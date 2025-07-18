package cn.com.edtechhub.workopenapi.common.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * 异常处理工具类
 */
@Slf4j
public class ThrowUtils {

    /**
     * 条件成立则抛异常, 并且打印消息日志
     */
    public static void throwIf(String message, boolean condition, ErrorCode errorCode) {
        if (condition) {
            log.warn(message);
            throw new BusinessException(errorCode, message);
        }
    }

}