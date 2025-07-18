package cn.com.edtechhub.workopenapi.common.exception;

import lombok.Getter;

/**
 * 业务内异常类
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Getter
public class BusinessException extends RuntimeException { /* RuntimeException 会触发事务回滚 */

    /**
     * 错误-含义
     */
    ErrorCode errorCode;

    /**
     * 详细信息
     */
    String exceptionMessage;

    /**
     * 构造异常对象
     *
     * @param errorCode 错误-含义 枚举体
     * @param exceptionMessage 详细信息
     */
    public BusinessException(ErrorCode errorCode, String exceptionMessage) {
        this.errorCode = errorCode;
        this.exceptionMessage = exceptionMessage;
    }

}
