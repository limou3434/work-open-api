package cn.com.edtechhub.testinterface;

import cn.com.edtechhub.workoapiclisdk.client.ApiClient;
import cn.com.edtechhub.workoapiclisdk.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TestInterfaceApplication {

    public static void main(String[] args) {
        // 启动服务端
        ConfigurableApplicationContext context = SpringApplication.run(TestInterfaceApplication.class, args);

        // 启动客户端(使用 SDK)
        ApiClient apiClient = context.getBean(ApiClient.class);
        User user = new User();
        user.setUsername("limou3434");
        String result1 = apiClient.getNameByGetWithParam("limou");
        String result2 = apiClient.getNameByPostWithParam("limou");
        String result3 = apiClient.getNameByPostWithRestful(user);
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);
    }

}
