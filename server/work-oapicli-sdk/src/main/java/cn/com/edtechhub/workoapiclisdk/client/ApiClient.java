package cn.com.edtechhub.workoapiclisdk.client;

import cn.com.edtechhub.workoapiclisdk.model.User;
import cn.com.edtechhub.workoapiclisdk.utils.SignUtils;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 给引入本 sdk 用户提供的客户端
 *
 * @author limou
 */
public class ApiClient {

    // 账号
    String accessKey;

    // 密码
    String secretKey;

    // 构造方法
    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    // GET 接口 (url 传参)
    public String getNameByGetWithParam(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.get("http://127.0.0.1:8123/api/name/get_name_by_get_with_param", paramMap);
    }

    // POST 接口 (url 传参)
    public String getNameByPostWithParam(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.post("http://127.0.0.1:8123/api/name/get_name_by_post_with_param", paramMap);
    }

    // 创建一个私有方法，用于构造请求头
    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", this.accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("body", body);
        // hashMap.put("secretKey", secretKey); // 禁止直接这么做
        // hashMap.put("sign", SignUtils.getSign(hashMap, this.secretKey)); // 没必要这么做
        hashMap.put("sign", SignUtils.getSign(body, this.secretKey)); // 这样做方便一些
        return hashMap;
    }

    // POST 接口 (Restful)
    public String getNameByPostWithRestful(User user) {
        // 将 User 对象转换为 JSON 字符串
        String json = JSONUtil.toJsonStr(user);

        // 使用 HttpRequest 工具发起 POST 请求，并获取服务器的响应
        HttpResponse httpResponse = HttpRequest
                .post("http://127.0.0.1:8123/api/name/get_name_by_post_with_restful/")
                .addHeaders(this.getHeaderMap(json))
                .body(json) // 将 JSON 字符串设置为请求体
                .execute(); // 执行请求

        // 返回服务器返回的结果
        return httpResponse.body();
    }

}
