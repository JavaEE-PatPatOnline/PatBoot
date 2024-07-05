package cn.edu.buaa.patpat.boot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsOptions {
    private String[] allowedOrigins;
    private String[] allowedMethods;
    private String[] allowedHeaders;
    private boolean allowCredentials;
}
