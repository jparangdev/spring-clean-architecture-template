package kr.co.jparangdev.transients.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TransientsModuleConfig.class)
public @interface EnableTransientsModule {
}
