package kr.co.jparangdev.messaging.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MessagingModuleConfig.class)
public @interface EnableMessagingModule {
}
