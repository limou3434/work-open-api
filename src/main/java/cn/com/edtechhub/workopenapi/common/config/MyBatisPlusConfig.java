package cn.com.edtechhub.workopenapi.common.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * MyBatis Plus 配置类
 *
 * @author <a href="https://github.com/limou3434">limou3434</a>
 */
@Configuration
@MapperScan("cn.com.edtechhub.workopenapi.mapper")
@Data
@Slf4j
public class MyBatisPlusConfig implements Config {

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


    /**
     * 添加分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL)); // 如果配置多个插件, 切记分页最后添加
        // 如果有多数据源可以不配具体类型, 否则都建议配上具体的 DbType
        return interceptor;
    }

}