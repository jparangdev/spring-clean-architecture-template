package kr.co.jparangdev.dal.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@Configuration
@EntityScan(basePackages = {"kr.co.jparangdev.dal.entity"})
@EnableJpaRepositories(basePackages = {"kr.co.jparangdev.dal.repository"})
public class JpaConfig {
}
