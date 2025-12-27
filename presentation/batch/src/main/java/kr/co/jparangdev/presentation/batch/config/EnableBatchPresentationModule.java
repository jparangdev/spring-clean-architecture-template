package kr.co.jparangdev.presentation.batch.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(BatchPresentationModuleConfig.class)
public @interface EnableBatchPresentationModule {
}
