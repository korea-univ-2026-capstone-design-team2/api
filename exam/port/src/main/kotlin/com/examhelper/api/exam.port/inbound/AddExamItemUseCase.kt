package com.examhelper.api.exam.port.inbound

import com.examhelper.api.exam.port.inbound.command.AddExamItemCommand

interface AddExamItemUseCase {
    fun execute(command: AddExamItemCommand)
}
