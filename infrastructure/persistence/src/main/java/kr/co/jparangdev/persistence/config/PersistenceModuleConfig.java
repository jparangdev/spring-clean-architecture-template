package kr.co.jparangdev.persistence.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JpaConfig.class)
@ComponentScan(basePackages = "kr.co.jparangdev.persistence")
public class PersistenceModuleConfig {
}
