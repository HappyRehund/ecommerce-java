package com.rehund.ecommerce.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Configuration
@Data
public class JwtSecretConfig {

    @Value("${app.jwtSecret}")
    private String jwtSecret; // buat ngambil jwtSecret dari application.properties

    @Value("${app.jwtExpirationTime}")
    private Duration jwtExpirationTime; // mirip

    @Bean
    public SecretKey signKey(){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    } // Nyediain bean signKey dengan type SecretKey
}
