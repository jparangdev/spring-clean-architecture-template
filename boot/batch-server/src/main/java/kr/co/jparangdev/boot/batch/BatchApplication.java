package kr.co.jparangdev.boot.batch;

import kr.co.jparangdev.application.config.EnableApplicationModule;
import kr.co.jparangdev.persistence.config.EnablePersistenceModule;
import kr.co.jparangdev.presentation.batch.config.EnableBatchPresentationModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableApplicationModule
@EnablePersistenceModule
@EnableBatchPresentationModule
@SpringBootApplication
public class BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}
