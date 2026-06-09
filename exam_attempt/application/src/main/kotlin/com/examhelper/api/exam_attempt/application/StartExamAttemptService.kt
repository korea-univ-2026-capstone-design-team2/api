package com.examhelper.api.exam_attempt.application

import com.examhelper.api.exam_attempt.domain.ExamAttempt
import com.examhelper.api.exam_attempt.domain.exception.ExamAttemptException
import com.examhelper.api.exam_attempt.port.inbound.StartExamAttemptUseCase
import com.examhelper.api.exam_attempt.port.inbound.command.StartExamAttemptCommand
import com.examhelper.api.exam_attempt.port.inbound.result.StartExamAttemptResult
import com.examhelper.api.exam_attempt.port.outbound.ExamAttemptStore
import com.examhelper.api.exam_attempt.port.outbound.ExamExistencePort
import com.examhelper.api.kernel.core.IdGenerator
import com.examhelper.api.kernel.identifier.ExamAttemptId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StartExamAttemptService(
    private val examExistencePort: ExamExistencePort,
    private val examAttemptStore: ExamAttemptStore,
    private val idGenerator: IdGenerator
) : StartExamAttemptUseCase {
    @Transactional
    override fun execute(command: StartExamAttemptCommand): StartExamAttemptResult {
        // ── 시험 존재 확인 ─────────────────────────────
        require(examExistencePort.existsById(command.examId)) { throw ExamAttemptException.ExamNotFound(command.examId.value) }

        // ── 진행중 응시 확인 ───────────────────────────

        // ── Aggregate 생성 ────────────────────────────
        val attempt = ExamAttempt.start(
            id = ExamAttemptId(idGenerator.generateId()),
            examId = command.examId,
            memberId = command.memberId
        )

        examAttemptStore.save(attempt)

        // ── 결과 반환 ─────────────────────────────────
        return StartExamAttemptResult(
            attemptId = attempt.id.value,
            examId = attempt.examId.value,
            status = attempt.status.name,
            startedAt = attempt.startedAt,
        )
    }
}
