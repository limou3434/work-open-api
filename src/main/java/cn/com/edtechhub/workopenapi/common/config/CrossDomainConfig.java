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
 * 跨域共享配置, 可以通过外部的应用配置文件来进行配置, 但是需要重启才能生效
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Configuration
@Slf4j
public class CrossDomainConfig implements WebMvcConfigurer, Config {

    // TODO: 后续更改为配置文件来进行跨域配置
    /**
     * 开发环境客户端主机
     */
    private static final String DEVELOPMENT_CLIENT_HOST = "http://127.0.0.1:8080";

    /**
     * 测试环境客户端主机
     */
    private static final String RELEASE_CLIENT_HOST = "http://测试主机IP:分配到的端口号300x";

    /**
     * 生产环境客户端主机
     */
    private static final String PRODUCTION_CLIENT_HOST = "https://xxx.edtechhub.com.cn";

    /**
     * 注入请求日志拦截切面依赖
     */
    @Resource
    private RequestLogAOP requestLogAOP;

    /**
     * 打印配置
     */
    @PostConstruct
    @Override
    public void printConfig() {
        log.debug(
                "[{}] 加载成功: {}",
                this.getClass().getName(),
                "配置允许跨域的 3 个主机, 对应三个环境: " + this.getCorsRule()
        );
    }

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
                DEVELOPMENT_CLIENT_HOST,
                RELEASE_CLIENT_HOST,
                PRODUCTION_CLIENT_HOST
        );
    }

}