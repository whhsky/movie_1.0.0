package com.movie.utils.rabbitmqConfig;


import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class rabbitmqConfig {

    public final static String NORMAL_EXCHANGE = "exchange.direct";
    public final static String DEAD_EXCHANGE = "exchange.dead";

    public final static String NORMAL_QUEUE = "normalQueue";
    public final static String DEAD_QUEUE = "DeadQueue";
    public final static String BINDING_NORMAL_KEY = "order";
    public final static String BINDING_DLX_KEY = "info";

    @Bean
    public DirectExchange normalExchange(){
        return new DirectExchange(NORMAL_EXCHANGE, true,false);
    }

    @Bean
    public DirectExchange deadExchange(){
        return new DirectExchange(DEAD_EXCHANGE, true,false);
    }

    @Bean
    public Queue normalQueue(){
        Map<String, Object> arguments =new HashMap<>();
        //指定死信交换机，通过x-dead-letter-exchange 来设置
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信路由key，value 为死信交换机和死信队列绑定的key，要一模一样，因为死信交换机是直连交换机
        arguments.put("x-dead-letter-routing-key",BINDING_DLX_KEY);
        //队列的过期时间10分钟
        arguments.put("x-message-ttl",600000);
        return new Queue(NORMAL_QUEUE,true, false, false, arguments);
    }

    @Bean
    public Queue deadQueue(){
        Map<String, Object> arguments =new HashMap<>();
        return new Queue(DEAD_QUEUE, true,false,false,arguments);
    }

    @Bean
    public Binding bindNormal(DirectExchange normalExchange, Queue normalQueue){
        return BindingBuilder.bind(normalQueue).to(normalExchange).with(BINDING_NORMAL_KEY);
    }


    @Bean
    public Binding bindDead(DirectExchange deadExchange, Queue deadQueue){
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(BINDING_DLX_KEY);
    }

}
