package cn.com.edtechhub.workopenapi.manger.satoken;

import cn.com.edtechhub.workopenapi.common.config.Config;
import cn.dev33.satoken.interceptor.SaInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

/**
 * Sa-token 配置类
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Configuration
@Slf4j
public class SaTokenConfig implements WebMvcConfigurer, Config {

    /**
     * 注册 Sa-Token 拦截器, 打开注解式鉴权功能
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
    }

    /**
     * 打印配置
     */
    @PostConstruct
    @Override
    public void printConfig() {
        log.debug(
                "[{}] 加载成功: {}",
                this.getClass().getName(),
                "配置了注解 MapperScan 来自动扫描数据库映射文件, 以及分页插件 PaginationInnerInterceptor"
        );
    }

}