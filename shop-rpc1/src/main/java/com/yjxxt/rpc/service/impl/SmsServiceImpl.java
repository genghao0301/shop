package com.yjxxt.rpc.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import com.yjxxt.rpc.service.ISmsService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

@Service(version = "1.0")
public class SmsServiceImpl implements ISmsService {

    @Value("${sms.endpoint}")
    private String endpoint;

    @Value("${sms.accessKeyId}")
    private String accessKeyId;

    @Value("${sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${sms.signName}")
    private String signName;

    @Value("${sms.templateCode}")
    private String templateCode;


    public  com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = endpoint;
        return new Client(config);
    }

    @Override
    public void sendSms(String phone) {
        try {
            Client client = this.createClient(accessKeyId, accessKeySecret);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phone)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam("{\"code\":\"1314\"}");
            String code = client.sendSms(sendSmsRequest).getBody().getCode();
            if(code.equals("OK")){
                System.out.println("短信发送成功!");
            }else{
                System.out.println("短信发送失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
