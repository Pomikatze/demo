package com.example.demo.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.demo.repositories",
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EntityScan("com.example.demo.entity")
@EnableJpaAuditing
public class JpaConfig {
}
