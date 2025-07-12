package cn.com.edtechhub.workoapiclisdk;


import cn.com.edtechhub.workoapiclisdk.client.ApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 读取用户配置后自动创建一个 apiClient Bean, 然后
 * 在 META-INF/spring.factories 中指定了要自动配置下面这个类
 * Spring Boot 将会在应用启动时自动加载和实例化 WorkOapicliSdkConfig 并将其应用于我们的应用程序中
 * 这样我们就可以使用自动配置生成的 apiClient 对象, 而无需手动创建和配置
 * 这种是 Spring Framework 提供的底层 SPI 扩展机制的一部分
 */
@Configuration // 通过 @Configuration 注解, 将该类标记为一个配置类, 告诉 Spring 这是一个用于配置的类
@ConfigurationProperties("work.oapicli.sdk") // 读取用户的 application.yaml 配置中前缀为 "work.oapicli.sdk" 的配置
@ComponentScan // 注解用于自动扫描组件，使得 Spring 能够自动注册相应的 Bean
@Data // Lombok 自动生成 getter/setter 的注解
public class WorkOapicliSdkConfig {

    /**
     * 账号
     */
    private String accessKey;

    /**
     * 密钥
     */
    private String secretKey;

    /**
     * 制作客户端 Bean
     *
     * @return 客户端
     */

    @Bean
    public ApiClient apiClient() {
        return new ApiClient(this.accessKey, this.secretKey);
    }

}
