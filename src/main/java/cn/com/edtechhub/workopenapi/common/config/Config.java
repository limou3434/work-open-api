package cn.com.edtechhub.workopenapi.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Config {

    /**
     * 打印配置
     */
    default void printConfig() {
        Logger log = LoggerFactory.getLogger(getClass()); // 动态获取实现类的Logger
        log.debug("[{}] 加载成功: {}", this.getClass().getName(), "配置类被启用");
    }

}
