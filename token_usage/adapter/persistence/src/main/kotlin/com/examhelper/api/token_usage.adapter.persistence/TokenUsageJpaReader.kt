package com.examhelper.api.token_usage.adapter.persistence

import com.examhelper.api.token_usage.adapter.persistence.projection.TokenUsageDailyStatisticsProjection
import com.examhelper.api.token_usage.adapter.persistence.projection.TokenUsageStatisticsProjection
import com.examhelper.api.token_usage.domain.type.AiModel
import com.examhelper.api.token_usage.domain.type.AiProvider
import com.examhelper.api.token_usage.domain.type.TokenUsageDomain
import com.examhelper.api.token_usage.domain.type.TokenUsageStatus
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageDailyStatisticsView
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageStatisticsView
import com.examhelper.api.token_usage.port.inbound.view.TokenUsageSummaryView
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant

interface TokenUsageJpaReader : JpaRepository<TokenUsageEntity, Long> {
    @Query("""
        SELECT new com.examhelper.api.token_usage.port.inbound.view.TokenUsageSummaryView(
            t.id,
            t.targetDomain,
            t.targetReferenceId,
            t.provider,
            t.model,
            t.promptTokens,
            t.completionTokens,
            t.totalTokens,
            t.totalCost,
            t.currency,
            t.status,
            t.createdAt
        )
        FROM TokenUsageEntity t
        WHERE (:targetDomain IS NULL OR t.targetDomain = :targetDomain)
          AND (:targetReferenceId IS NULL OR t.targetReferenceId = :targetReferenceId)
          AND (:provider IS NULL OR t.provider = :provider)
          AND (:model IS NULL OR t.model = :model)
          AND (:status IS NULL OR t.status = :status)
          AND (:from IS NULL OR t.createdAt >= :from)
          AND (:to IS NULL OR t.createdAt <= :to)
        ORDER BY t.createdAt DESC
    """)
    fun findSummaries(
        @Param("targetDomain") targetDomain: TokenUsageDomain?,
        @Param("targetReferenceId") targetReferenceId: Long?,
        @Param("provider") provider: AiProvider?,
        @Param("model") model: AiModel?,
        @Param("status") status: TokenUsageStatus?,
        @Param("from") from: Instant?,
        @Param("to") to: Instant?,
        pageable: Pageable,
    ): List<TokenUsageSummaryView>

    @Query("""
        SELECT COUNT(t)
        FROM TokenUsageEntity t
        WHERE (:targetDomain IS NULL OR t.targetDomain = :targetDomain)
          AND (:targetReferenceId IS NULL OR t.targetReferenceId = :targetReferenceId)
          AND (:provider IS NULL OR t.provider = :provider)
          AND (:model IS NULL OR t.model = :model)
          AND (:status IS NULL OR t.status = :status)
          AND (:from IS NULL OR t.createdAt >= :from)
          AND (:to IS NULL OR t.createdAt <= :to)
    """)
    fun countByFilter(
        @Param("targetDomain") targetDomain: TokenUsageDomain?,
        @Param("targetReferenceId") targetReferenceId: Long?,
        @Param("provider") provider: AiProvider?,
        @Param("model") model: AiModel?,
        @Param("status") status: TokenUsageStatus?,
        @Param("from") from: Instant?,
        @Param("to") to: Instant?,
    ): Long

    @Query("""
        SELECT
            COUNT(t) AS totalRequests,
            COALESCE(SUM(t.promptTokens), 0) AS totalPromptTokens,
            COALESCE(SUM(t.completionTokens), 0) AS totalCompletionTokens,
            COALESCE(SUM(t.totalTokens), 0) AS totalTokens,
            COALESCE(SUM(t.totalCost), 0) AS totalCost
        FROM TokenUsageEntity t
        WHERE (:targetDomain IS NULL OR t.targetDomain = :targetDomain)
          AND (:targetReferenceId IS NULL OR t.targetReferenceId = :targetReferenceId)
          AND (:provider IS NULL OR t.provider = :provider)
          AND (:model IS NULL OR t.model = :model)
          AND (:status IS NULL OR t.status = :status)
          AND (:from IS NULL OR t.createdAt >= :from)
          AND (:to IS NULL OR t.createdAt <= :to)
    """)
    fun findStatistics(
        @Param("targetDomain") targetDomain: TokenUsageDomain?,
        @Param("targetReferenceId") targetReferenceId: Long?,
        @Param("provider") provider: AiProvider?,
        @Param("model") model: AiModel?,
        @Param("status")
        status: TokenUsageStatus?,
        @Param("from") from: Instant?,
        @Param("to") to: Instant?
    ): TokenUsageStatisticsProjection

    // ── Daily Statistics ────────────────────────────────
    @Query("""
        SELECT 
            FUNCTION('DATE', t.createdAt) AS date,
            COUNT(t) AS totalRequests,
            COALESCE(SUM(t.promptTokens), 0) AS totalPromptTokens,
            COALESCE(SUM(t.completionTokens), 0) AS totalCompletionTokens,
            COALESCE(SUM(t.totalTokens), 0) AS totalTokens,
            COALESCE(SUM(t.totalCost), 0) AS totalCost
        FROM TokenUsageEntity t
        WHERE (:targetDomain IS NULL OR t.targetDomain = :targetDomain)
          AND (:targetReferenceId IS NULL OR t.targetReferenceId = :targetReferenceId)
          AND (:provider IS NULL OR t.provider = :provider)
          AND (:model IS NULL OR t.model = :model)
          AND (:status IS NULL OR t.status = :status)
          AND (:from IS NULL OR t.createdAt >= :from)
          AND (:to IS NULL OR t.createdAt <= :to)
        GROUP BY FUNCTION('DATE', t.createdAt)
        ORDER BY FUNCTION('DATE', t.createdAt) ASC
    """)
    fun findDailyStatistics(
        @Param("targetDomain") targetDomain: TokenUsageDomain?,
        @Param("targetReferenceId") targetReferenceId: Long?,
        @Param("provider") provider: AiProvider?,
        @Param("model") model: AiModel?,
        @Param("status") status: TokenUsageStatus?,
        @Param("from") from: Instant?,
        @Param("to") to: Instant?,
    ): List<TokenUsageDailyStatisticsProjection>

    // ── Model Statistics ────────────────────────────────
    /*
    @Query("""
        SELECT new com.examhelper.api.token_usage.port.inbound.view.TokenUsageModelStatisticsView(
            t.model,

            COUNT(t),

            COALESCE(SUM(t.promptTokens), 0),
            COALESCE(SUM(t.completionTokens), 0),
            COALESCE(SUM(t.totalTokens), 0),

            COALESCE(SUM(t.totalCost), 0)
        )
        FROM TokenUsageEntity t
        WHERE (:targetDomain IS NULL OR t.targetDomain = :targetDomain)
          AND (:targetReferenceId IS NULL OR t.targetReferenceId = :targetReferenceId)
          AND (:provider IS NULL OR t.provider = :provider)
          AND (:model IS NULL OR t.model = :model)
          AND (:status IS NULL OR t.status = :status)
          AND (:from IS NULL OR t.createdAt >= :from)
          AND (:to IS NULL OR t.createdAt <= :to)
        GROUP BY t.model
        ORDER BY SUM(t.totalCost) DESC
    """)
    fun findModelStatistics(
        @Param("targetDomain")
        targetDomain: TokenUsageDomain?,

        @Param("targetReferenceId")
        targetReferenceId: Long?,

        @Param("provider")
        provider: AiProvider?,

        @Param("model")
        model: AiModel?,

        @Param("status")
        status: TokenUsageStatus?,

        @Param("from")
        from: Instant?,

        @Param("to")
        to: Instant?,
    ): List<TokenUsageModelStatisticsView>
     */
}
