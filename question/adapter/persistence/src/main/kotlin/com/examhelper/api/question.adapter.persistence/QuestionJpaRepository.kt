package com.examhelper.api.question.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Question 엔티티에 대한 JPA Repository 인터페이스입니다.
 * Spring Data JPA가 이 인터페이스를 구현하여 기본적인 CRUD 작업을 제공합니다.
 */
interface QuestionJpaRepository: JpaRepository<QuestionEntity, Long>
