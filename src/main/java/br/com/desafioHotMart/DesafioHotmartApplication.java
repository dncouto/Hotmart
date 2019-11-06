package br.com.desafioHotMart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class DesafioHotmartApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DesafioHotmartApplication.class, args);
    }

}