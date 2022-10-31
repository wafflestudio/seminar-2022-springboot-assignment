package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.CreateSeminarResponse
import com.wafflestudio.seminar.core.seminar.domain.EditSeminarResponse
import com.wafflestudio.seminar.core.seminar.domain.SeminarPort
import org.springframework.stereotype.Service

@Service
class SeminarService(
    private val seminarPort: SeminarPort
) {
    fun createSeminar(userId: Long, createSeminarRequest: CreateSeminarRequest): CreateSeminarResponse {
        return seminarPort.createSeminar(userId, createSeminarRequest)
    }

    fun editSeminar(userId: Long, editSeminarRequest: EditSeminarRequest): EditSeminarResponse {
        return seminarPort.editSeminar(userId, editSeminarRequest)
    }
}