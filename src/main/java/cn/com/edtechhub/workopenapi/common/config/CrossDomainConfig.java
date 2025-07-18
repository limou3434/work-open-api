package cn.com.edtechhub.workopenapi.common.config;

import cn.com.edtechhub.workopenapi.common.aop.RequestLogAOP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 跨域共享配置
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Configuration
@Slf4j
public class CrossDomainConfig implements WebMvcConfigurer {

    /**
     * 注入请求日志拦截切面依赖
     */
    @Resource
    private RequestLogAOP requestLogAOP;

    /**
     * 配合切面拦截所有接口调用以提供详细的访问日志打印
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogAOP).addPathPatterns("/**");
    }

    /**
     * 配置跨域共享
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOriginPatterns(this.getCorsRule().toArray(new String[0]))
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 允许跨域规则
     */
    private List<String> getCorsRule() {
        return Arrays.asList(
                "http://127.0.0.1:3000", // 开发环境
                "http://测试主机IP:分配到的端口号300x", // 测试环境
                "https://xxx.edtechhub.com.cn" // 生产环境
        );
    }

    /**
     * 打印配置
     */
    @PostConstruct
    public void printConfig() {
        log.debug("当前项目 Cors 跨域规则为 {}", this.getCorsRule());
    }

}