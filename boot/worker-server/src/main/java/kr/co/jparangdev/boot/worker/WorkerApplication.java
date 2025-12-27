package kr.co.jparangdev.boot.worker;

import kr.co.jparangdev.application.config.EnableApplicationModule;
import kr.co.jparangdev.transients.config.EnableTransientsModule;
import kr.co.jparangdev.persistence.config.EnablePersistenceModule;
import kr.co.jparangdev.messaging.config.EnableMessagingModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApplicationModule
@EnablePersistenceModule
@EnableTransientsModule
@EnableMessagingModule
@SpringBootApplication
public class WorkerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }
}
