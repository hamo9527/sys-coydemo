package com.cailu.bom.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cailu.jwt")
public class JwtProperties {

    private String secret;
    private long expireSeconds = 86400;
}
