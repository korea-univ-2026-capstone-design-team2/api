package com.examhelper.api.exam.port.inbound

import com.examhelper.api.exam.port.inbound.command.AddExamItemCommand

interface AddExamItemUseCase {
    fun addItem(command: AddExamItemCommand)
}
