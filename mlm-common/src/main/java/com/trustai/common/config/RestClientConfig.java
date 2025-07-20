package com.trustai.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient userServiceRestClient(RestClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080/api/v1/provider").build();
    }

    @Bean
    public RestClient v1ApiRestClient(RestClient.Builder builder) {
        return builder.baseUrl("http://localhost:8080/api/v1").build();
    }
}
