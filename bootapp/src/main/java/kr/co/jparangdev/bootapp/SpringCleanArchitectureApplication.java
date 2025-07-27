package kr.co.jparangdev.bootapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"kr.co.jparangdev"})
@EntityScan(basePackages = {"kr.co.jparangdev.domain"})
public class SpringCleanArchitectureApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCleanArchitectureApplication.class, args);
    }

}
