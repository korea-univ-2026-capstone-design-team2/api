package com.examhelper.api.infrastructure.persistence

import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan(basePackages = ["com.examhelper.api"])
@EnableJpaRepositories(basePackages = ["com.examhelper.api"])
class JpaConfig
