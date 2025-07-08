package cn.com.edtechhub.workopenapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 签名认证实现
 * 通过 http request header 头传递参数
 * 参数 1：accessKey 账号, 调用时的标识区分 userA, userB(复杂、无序、无规律、无状态)
 * 参数 2：secretKey 密钥, 该参数不能放到请求头中(复杂、无序、无规律、无状态)
 * 参数 3：params 用户请求参数, 用来参与制作签名, 就算是参数被中间人篡改, 也会导致签名不一致
 * 参数 4：sign 签名, 把 "参数 + 密钥" 进行加密得到签名, 签名一致才可能发生调用, 之所以需要两者结合, 是为了避免一次请求不小心暴露时就被获取密钥, 加密方式为 "对称加密、非对称加密、md5 哈系签名", 实现原理和 HTTP(s) 实际上是一样的, 但是我们需要在业务层也实现一遍, 进行一个双重加密
 * 参数 5：nonce 随机数, 这个随机数只能"用一次(其实就是短时间内进行标记)", 之所以需要这么做是因为用户在第一次发送报文的时候的确加密了参数和密钥，其他攻击者确实看不到密钥, 但是也不用看不到, 只需要复制一份加密的签名跟在后面继续调用就破解了...
 * 参数 6：加 timestamp 时间戳, 校验时间戳是否过期, 这样哪怕经过一段时间后的 nonce 恢复为没有 "使用过" 的状态时, 由于超过规定时间也无法拷贝签名来调用
 */
@SpringBootApplication
public class WorkOpenApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkOpenApiApplication.class, args);
    }

}
