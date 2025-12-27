package kr.co.jparangdev.presentation.batch.user;

import kr.co.jparangdev.application.user.UserBatchUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration for the dormant user batch job.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DormantUserJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserBatchUseCase userBatchUseCase;

    @Bean
    public Job dormantUserJob() {
        return new JobBuilder("dormantUserJob", jobRepository)
                .start(dormantUserStep())
                .build();
    }

    @Bean
    public Step dormantUserStep() {
        return new StepBuilder("dormantUserStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    int processedCount = userBatchUseCase.processDormantUsers();
                    log.info("Batch job 'dormantUserJob' processed {} users.", processedCount);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
