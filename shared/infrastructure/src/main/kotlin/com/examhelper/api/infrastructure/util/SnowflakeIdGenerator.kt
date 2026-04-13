package com.examhelper.api.infrastructure.util

import com.examhelper.api.kernel.core.IdGenerator
import org.springframework.stereotype.Component
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Component
class SnowflakeIdGenerator(
    private val properties: SnowflakeProperties,
) : IdGenerator {

    private val epoch = 1_680_000_000_000L
    private val datacenterIdBits = 5L
    private val workerIdBits = 5L
    private val sequenceBits = 12L
    private val maxDatacenterId = -1L xor (-1L shl datacenterIdBits.toInt())
    private val maxWorkerId = -1L xor (-1L shl workerIdBits.toInt())
    private val sequenceMask = -1L xor (-1L shl sequenceBits.toInt())
    private val workerIdShift = sequenceBits
    private val datacenterIdShift = sequenceBits + workerIdBits
    private val timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits

    private val lock = ReentrantLock()
    private var lastTimestamp = -1L
    private var sequence = 0L

    init {
        require(properties.datacenterId in 0..maxDatacenterId) {
            "datacenterId는 0에서 $maxDatacenterId 사이여야 합니다"
        }
        require(properties.workerId in 0..maxWorkerId) {
            "workerId는 0에서 $maxWorkerId 사이여야 합니다"
        }
    }

    override fun generateId(): Long = lock.withLock {
        var timestamp = currentTime()

        if (timestamp < lastTimestamp) {
            throw IllegalStateException("시계가 뒤로 이동했습니다. 마지막 타임스탬프: $lastTimestamp, 현재 타임스탬프: $timestamp. " +
                    "${lastTimestamp - timestamp}ms 만큼의 차이가 발생했습니다.")
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) and sequenceMask
            if (sequence == 0L) timestamp = waitNextMillis(lastTimestamp)
        } else {
            sequence = 0L
        }
        lastTimestamp = timestamp

        ((timestamp - epoch) shl timestampLeftShift.toInt()) or
                ((properties.datacenterId and maxDatacenterId) shl datacenterIdShift.toInt()) or
                ((properties.workerId and maxWorkerId) shl workerIdShift.toInt()) or
                sequence
    }

    private fun waitNextMillis(lastTimestamp: Long): Long {
        val deadline = lastTimestamp + 5L
        var timestamp = currentTime()
        while (timestamp <= lastTimestamp) {
            if (timestamp > deadline) throw IllegalStateException("\"시간이 역전되었습니다. 마지막 타임스탬프: \$lastTimestamp, 현재 타임스탬프: \$currentTimestamp. \" +\n" +
                    "    \"\${lastTimestamp - currentTimestamp}ms 만큼의 차이가 발생했습니다.\"")
            Thread.onSpinWait()
            timestamp = currentTime()
        }
        return timestamp
    }

    private fun currentTime(): Long = System.currentTimeMillis()
}
