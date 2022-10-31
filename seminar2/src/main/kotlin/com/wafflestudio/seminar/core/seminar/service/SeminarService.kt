package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.SeminarPort
import com.wafflestudio.seminar.core.seminar.domain.SeminarResponse
import org.springframework.stereotype.Service

@Service
class SeminarService(
    private val seminarPort: SeminarPort
) {
    fun createSeminar(userId: Long, createSeminarRequest: CreateSeminarRequest): SeminarResponse {
        return seminarPort.createSeminar(userId, createSeminarRequest)
    }

    fun editSeminar(userId: Long, editSeminarRequest: EditSeminarRequest): SeminarResponse {
        return seminarPort.editSeminar(userId, editSeminarRequest)
    }
}