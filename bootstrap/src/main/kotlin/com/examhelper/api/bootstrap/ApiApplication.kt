package com.examhelper.api.bootstrap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.examhelper.api"])
@ConfigurationPropertiesScan(basePackages = ["com.examhelper.api"])
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
