package org.trananh.shoppingappbackend;

import org.springframework.boot.SpringApplication;	
import org.springframework.boot.autoconfigure.SpringBootApplication;	
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;	
	
@EntityScan(basePackages = {"org.trananh.shoppingappbackend.model"})	
@EnableJpaRepositories(basePackages = {"org.trananh.shoppingappbackend.repository"})
@SpringBootApplication
public class ShoppingAppBackendApplication {	
	
	public static void main(String[] args) {
		SpringApplication.run(ShoppingAppBackendApplication.class, args);	
	}
}
