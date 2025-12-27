package kr.co.jparangdev.persistence.config;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing
@EnableTransactionManagement
@Configuration
@EntityScan(basePackages = { "kr.co.jparangdev.persistence" })
@EnableJpaRepositories(basePackages = { "kr.co.jparangdev.persistence" })
public class JpaConfig {
}
