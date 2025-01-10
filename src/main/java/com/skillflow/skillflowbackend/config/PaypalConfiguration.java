package com.skillflow.skillflowbackend.config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfiguration {
    @Value("${paypal.clientId}")
    private String clientId;
    @Value("${paypal.clientSecret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;
    @Bean
    public APIContext apiContext(){
        return new APIContext(clientId, clientSecret, mode);
    }
}
