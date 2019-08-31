package me.hchome.demouser;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoUserApplication extends SpringBootServletInitializer {
    @Value("${me.hchome.jwt.secret:P@ssw0rd}")
    private String secret;


    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoUserApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(DemoUserApplication.class);
    }
}
