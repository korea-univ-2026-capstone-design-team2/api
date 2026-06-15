package com.examhelper.api.exam.port.outbound

import com.examhelper.api.exam.port.inbound.query.ExamFilter
import com.examhelper.api.exam.port.inbound.view.ExamBaseView
import com.examhelper.api.exam.port.inbound.view.ExamDetailView
import com.examhelper.api.exam.port.inbound.view.ExamSummaryView

interface ExamReader {
    fun findDetailById(examId: Long): ExamDetailView?
    fun findDetailById(examId: Long, memberId: Long): ExamDetailView?
    fun findSummaries(filter: ExamFilter): List<ExamSummaryView>
    fun findBaseById(examId: Long): ExamBaseView?
    fun findBaseById(examId: Long, memberId: Long): ExamBaseView?
    fun count(filter: ExamFilter): Long
    fun existsById(examId: Long, memberId: Long): Boolean
}
