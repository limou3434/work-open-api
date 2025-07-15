package cn.com.edtechhub.workopenapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 签名认证实现:
 * -> 用户在 Open API 服务中注册账号获得密钥
 * -> 用户根据文档引入 SDK 后使用其中的接口
 * -> 其他用户服务需要收集 "账号、请求体、随机数、时间戳" 作为参数, 并且都作为报头的参数
 * -> 其他用户服务在调用请求的时候就把用户的 "参数+密钥" 生成 "签名", 并且把也作为报头的参数
 * -> 其他用户服务需要封装调用接口似需要的参数, 这些参数通过 json 装化为 "载荷"
 * -> 开发接口服务通过报文解析获得 "报头" 和 "载荷"
 * -> 开发接口服务通过 "报头" 来鉴权, 通过 "载荷" 来调用
 * 通过 http request header 头传递参数, 这里介绍一下参数的作用
 * 参数 1：accessKey 账号, 调用时的标识区分 userA, userB(复杂、无序、无规律、无状态)
 * 参数 2：secretKey 密钥, 该参数不能直接放到请求头中(复杂、无序、无规律、无状态)
 * 参数 3：params 用户请求参数, 这里的参数作用很泛, 不过一般用 body 来直接传递, 主要是用来参与签名的制作, 这样就算是参数被中间人篡改, 也会导致签名不一致
 * 参数 4：sign 签名, 把 "参数 + 密钥" 进行加密得到签名, 签名一致才可能发生调用, 之所以需要两者结合, 是为了避免一次请求不小心暴露时就被获取密钥, 加密方式为 "对称加密、非对称加密、md5 哈系签名", 实现原理和 HTTP(s) 实际上是一样的, 但是我们需要在业务层也实现一遍, 进行一个双重加密
 * 参数 5：nonce 随机数, 这个随机数只能"用一次(其实就是短时间内标记为使用了一次, 不能直接发起一模一样的请求)", 之所以需要这么做是因为用户在第一次发送报文的时候的确加密了参数和密钥，其他攻击者确实看不到密钥, 但是也不用看不到, 只需要复制一份加密的签名跟在后面继续调用(比如浏览器都自带的 XHR 功能), 就破解了...
 * 参数 6：timestamp 时间戳, 校验时间戳是否过期, 这样哪怕经过一段时间后的 nonce 恢复为没有 "使用过" 的状态时, 由于超过规定时间也无法拷贝签名来调用
 * 可能有人会询问, 如果在使用 HTTP(s) 的情况下本身就有加密, 为什么还需要上的加密过程呢?
 * 很简单, 主要有以下理由: HTTP(s) 只能做到无法明文传输, 无法解决其他问题
 * (1)身份校验问题, 签名的"账号+密钥"的机制可以识别用户是否为数据库中已有的用户
 * (2)时间有效问题, 签名机制有效期短, 不会导致用户因为长期使用一个密钥就被破解, 也就是不断产生一个临时凭证
 * (3)防止重放问题, 仅仅靠纯粹的 HTTP(S) 无法避免重放攻击
 */
@SpringBootApplication
@Slf4j
public class WorkOpenApiApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WorkOpenApiApplication.class, args);
        log.debug("Spring Boot running...");
    }

}
