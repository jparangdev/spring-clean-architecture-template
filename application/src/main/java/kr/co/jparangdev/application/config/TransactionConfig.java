package kr.co.jparangdev.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 트랜잭션 설정
 * TransactionTemplate을 사용하여 명시적으로 트랜잭션 경계를 관리합니다.
 * 이는 @Transactional의 self-invocation 문제를 회피하고, 트랜잭션 경계를 명확하게 합니다.
 */
@Configuration
public class TransactionConfig {

    /**
     * 쓰기 작업용 TransactionTemplate
     */
    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    /**
     * 읽기 전용 TransactionTemplate
     * 읽기 작업의 성능 최적화를 위해 readOnly=true 설정
     */
    @Bean
    public TransactionTemplate readOnlyTransactionTemplate(PlatformTransactionManager transactionManager) {
        TransactionTemplate template = new TransactionTemplate(transactionManager);
        template.setReadOnly(true);
        return template;
    }
}
