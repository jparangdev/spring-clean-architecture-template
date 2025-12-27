package kr.co.jparangdev.boot.api;

import kr.co.jparangdev.application.config.EnableApplicationModule;
import kr.co.jparangdev.transients.config.EnableTransientsModule;
import kr.co.jparangdev.persistence.config.EnablePersistenceModule;
import kr.co.jparangdev.messaging.config.EnableMessagingModule;
import kr.co.jparangdev.notification.config.EnableNotificationModule;
import kr.co.jparangdev.presentation.api.config.EnableApiPresentationModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApplicationModule
@EnablePersistenceModule
@EnableTransientsModule
@EnableMessagingModule
@EnableNotificationModule
@EnableApiPresentationModule
@SpringBootApplication
public class SpringCleanArchitectureApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCleanArchitectureApplication.class, args);
    }

}
