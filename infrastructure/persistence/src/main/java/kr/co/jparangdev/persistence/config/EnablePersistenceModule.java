package kr.co.jparangdev.persistence.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PersistenceModuleConfig.class)
public @interface EnablePersistenceModule {
}
