package com.yjxxt.portal.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置类
 *
 * 
 * @since 1.0.0
 */
@Configuration
public class RabbitmqConfig {
    /**
     * 短信队列
     * @return
     */
    @Bean
    public Queue smsQueue(){
        return new Queue("smsQueue");
    }


    /**
     * topic交换机,topic模式
     * @return
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange("smsExchange");
    }

    /**
     * 将短信队列绑定到交换机
     * @return
     */
    @Bean
    public Binding infoBinding(){
        return BindingBuilder.bind(smsQueue()).to(topicExchange()).with("*.sms");
    }
}