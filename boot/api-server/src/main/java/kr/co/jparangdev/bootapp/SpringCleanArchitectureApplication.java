package kr.co.jparangdev.bootapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"kr.co.jparangdev"})
public class SpringCleanArchitectureApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCleanArchitectureApplication.class, args);
    }

}
