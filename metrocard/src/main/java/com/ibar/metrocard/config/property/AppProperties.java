package com.ibar.metrocard.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String jwtSecret;
    private String jwtRefreshSecret;
    private Long jwtExpiration;
    private Long jwtRefreshExpiration;

    public Long getJwtRefreshExpiration(){
        return jwtRefreshExpiration * 60000;
    }

    public Long getJwtExpiration(){
        return jwtExpiration * 60000;
    }
}
