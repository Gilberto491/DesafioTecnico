package com.sicredi.desafio.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "callback.url")
@Getter
@Setter
public class CallbackUrlConfig {

    private String domain;
}
