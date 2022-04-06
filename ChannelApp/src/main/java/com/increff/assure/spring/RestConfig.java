package com.increff.assure.spring;

import com.increff.assure.service.ClientWrapper;
import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

    @Bean
    public RestTemplate restTemplate() {


        var factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(45000);
        factory.setReadTimeout(45000);//TODO 45sec

        return new RestTemplate(factory);
    }

    @Bean
    public ClientWrapper clientWrapper(){
        return new ClientWrapper();
    }


}
