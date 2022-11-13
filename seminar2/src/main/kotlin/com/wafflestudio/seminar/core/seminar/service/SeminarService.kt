package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.SearchSeminarResponse
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

    fun getSeminar(seminarId: Long): SeminarResponse {
        return seminarPort.getSeminar(seminarId)
    }

    fun searchSeminar(name: String? = null, order: String? = null): List<SearchSeminarResponse> {
        return seminarPort.searchSeminar(name = name, order = order)
    }

    fun joinSeminar(seminarId: Long, userId: Long, joinSeminarRequest: JoinSeminarRequest): SeminarResponse {
        return seminarPort.joinSeminar(seminarId = seminarId, userId = userId, joinSeminarRequest)
    }

    fun dropSeminar(seminarId: Long, userId: Long): SeminarResponse {
        return seminarPort.dropSeminar(seminarId = seminarId, userId = userId)
    }
}