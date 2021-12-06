package com.yjxxt.order.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000118660136";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCcEy50StI+NieRPiuLjXV5mc0ipKFvgAM1rfts6oAGx7n5Y2yzqmVwqtJokBFNoIC9/IakShaTfLr5d/tWgMMNpvOUkamMUmfbDWZoGsEYAqSDMFZ489Hli6ChKKq7vNxlLWBmHV0qfrEhIPBbwIdzQSle2tdPw+Djue+8Zps9atTwTiQFqXHFynepj9m/qrWoX8IZiC3SrA1gXNox/ZvuY0b7ZrSYJXxjXAJZMazzeOXlMHdVRW6s7dIMAcLrv/SnF0Akej94jS664t5wxlH04LUt3mImZBW9LUU/mQv8v8XFDCfSEbzqbUalIYDdp6sqxZ0sQq71dsofunoUMOwhAgMBAAECggEAVLYoiaAuuuuD0FaRJf6Sm3y6FaqLoMw3ccRL6hLhiEyRiWXIkz/SLTPaXjyL17ldB0FwBu3bkROrHEpeFdZviBJstN/Wyb3UdtWwFqMm2RReHIJ4/M9BxrhhZ/d9q8ZXTWcyolofvjfjhhqtuu8l1Y2xmbVoFfg4MQIzt+ttKQjctvnsKQIxLOCAI8vUu2zqQPzR5yeCNeuwhxNpvSklga4WvKTVf3FLdQoR7emeeu+Xk3bI+NWU9x1jsFnIAnimoUfaaGbPJJ72PWW9g7OG+SXTLjPJ88QBjMu1241gUY9pWR5CKP96pMhTniAHpBYphkswql84mzXmerRFOuAukQKBgQD0uMD6e+JNb52nXGHBikivbVrg6MoNN+cPgFZx1bfpBUzLrho7ssNb/VZM0QvtgnP3h5o9CkBS+8yAmIEuY2u0Npmu42vOGlWmaFzs5H24poGD+O3uL0A52Qnb6CMl1AY6f/flE9xzeRa95UiwL3GXGvLVDNbA6m+avKII6uU9VQKBgQCjRJDjplyBKVG7x/OlG2l0LYnt1eLqR4uqVeGnkQMD2QjVWA3O3FacvmOQUvkWoKtTi8ftGiRZ6xOqx9ijeK+ACeccnqhLL3569IZlyBFWHlO0IdMYiEXEsnMBFCHRpCkJ5SBFMf56YwQ2pOs0cVdsnbY3tqLMidy0ReRx8dkTnQKBgQDWifm0HlMgq888X4Lb100tbeMT9UDdIbsRHeOq3D/kqt7OWQ1qdKdLx+aWZVXdnFXqFRkklg4aMeDwg8IMVuCfFpbYeUuTXgbkYe3FR/LnpCE6/0onW4kkrZ81CGd3zE7BjlpWm3jSSr+jLpTw0Eb2v4tPN3g1m9DHiMNMqhDaGQKBgA7lxsK+/1nZx1d3G0hJhAnRzfAhwB7YgGn+hCSiGsBTIXc2Haxudoi+5p7Ys1nZN0jcTFXaOM7roFPJAH+KF5l7TddSstCJeOUHQuwuoUZWZhTdsUFfzd3w2oWSrLNQY14/xf4KXcoVFuFVkxCp9uw2R9Y1mA1J1xWr+vhatRARAoGBAIRBd4VY1sCbn8gHAm5JIDAbHBo5CTtV0plyhos8YmSE6+HEvcmqO5E8wT60mxOm+qDO/g6F1QnBfdSPZvSvXut62iTVw0gdAcqxSXwVfUGfA1QlwCvCjzavGdy4fcSRnbUQFmBDp8FGbpDva3rz4IJYqDb0eOK7alBEglIVza4e";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmnebuEJIkbSl9tXW0T96CyHvY5jKxcGphyq7noUdrB25FJz/hzCp+wPphYdO5XsCYHpP0+9B8te/7hwwSQ/COK4U2kIfyJxeV5uYQmqRDCyLwx7FQjMOxKObY4U1LOx89Q9bMEJFEOmKIeKRYIQppoVcDv4V1ReQ50O/rsZRSjHBE99Pt2I7Zst8FfIdcZDDvKIHMjMZQOmSB6BO2xjxw440nL9k9axcXimCsi7oM6nsU21o9Jy/nGe0J8/ib3ezWe8fKlH1ARH5XSSUs7NNymk43ctmNqvjz8bSWnmWV2SPy6ymLW3LfM6YH99RCpRR797+tOyNOKqZCOi6ihwt+QIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:9092/shop-order/order/notifyCallback";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:9092/shop-order/order/returnCallback";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

