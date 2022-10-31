package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest

interface SeminarPort {
    fun createSeminar(userId: Long, createSeminarRequest: CreateSeminarRequest): SeminarResponse

    fun editSeminar(userId: Long, editSeminarRequest: EditSeminarRequest): SeminarResponse

    fun getSeminar(seminarId: Long): SeminarResponse

    fun searchSeminar(name: String?, order: String?): List<SearchSeminarResponse>

    fun joinSeminar(seminarId: Long, userId: Long, joinSeminarRequest: JoinSeminarRequest): SeminarResponse
}