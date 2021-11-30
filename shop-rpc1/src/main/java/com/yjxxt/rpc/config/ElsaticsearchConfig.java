package com.yjxxt.rpc.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElsaticsearchConfig {

   //ES服务器地址
    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private Integer port;


    //ES服务器连接方式
    public static final String SCHEME="HTTP";



    /**
     * 创建RestClientBuilder对象
     * @return
     */
    @Bean
    public RestClientBuilder restClientBuilder(){
        return RestClient.builder(new HttpHost(host,port,SCHEME));
    }

    /**
     * 创建RestHighLevelClient对象
     * @param restClientBuilder
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient(@Autowired RestClientBuilder restClientBuilder){
        return new RestHighLevelClient(restClientBuilder);
    }
}