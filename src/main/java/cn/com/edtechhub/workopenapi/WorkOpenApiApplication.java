package cn.com.edtechhub.workopenapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class WorkOpenApiApplication {

    /**
     * 启动方法, 启动前需要配置 .env-template 文件中的环境变量
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WorkOpenApiApplication.class, args);
        log.debug(
                "Spring Boot 正常启动\n" +
                        "=======================================================================================================\n" +
                        "访问 {} 或 {} 获取在线调试文档\n" +
                        "访问 {} 即获取在线文档配置 json\n" +
                        "可以使用 {} 分析慢查语句, 参考 {}\n" +
                        "可以使用 {} 分析调用堆栈, 参考 {}\n" +
                        "可以使用 {} 分析响应时长, 参考 {}\n" +
                        "======================================================================================================="
                , "http://127.0.0.1:8000/doc.html"
                , "http://127.0.0.1:8000/swagger-ui/index.html"
                , "http://127.0.0.1:8000/v3/api-docs"
                , "http://127.0.0.1:8000/druid/login.html", "https://github.com/alibaba/druid/wiki/"
                , "本项目父目录下的脚本文件 ", "https://www.ruanyifeng.com/blog/2017/09/flame-graph.html"
                , "谷歌浏览器 F12 处的 Network 选项卡", "https://developer.chrome.com/docs?hl=zh-cn"
        );
    }

    /*
    本项目的待定任务: ...
     */

}
