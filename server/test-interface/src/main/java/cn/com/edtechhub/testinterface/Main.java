package cn.com.edtechhub.testinterface;

import cn.com.edtechhub.workoapiclisdk.client.ApiClient;
import cn.com.edtechhub.workoapiclisdk.model.User;

import javax.annotation.Resource;

public class Main {

    @Resource
    private ApiClient apiClient;

    public static void main(String[] args) {
        String accessKey = "limou3434";
        String secretKey = "abcdefgh";
        ApiClient apiClient = new ApiClient(accessKey, secretKey);
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
