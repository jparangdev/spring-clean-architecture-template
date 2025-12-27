package kr.co.jparangdev.presentation.api.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ApiPresentationModuleConfig.class)
public @interface EnableApiPresentationModule {
}
