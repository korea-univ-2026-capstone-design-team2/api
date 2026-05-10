package com.examhelper.api.question_generation.adapter.persistence.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "frame-search")
data class FrameSearchProperties(
    val scoreThreshold: Double = 0.60,
    val postFilterMultiplier: Int = 3
)
