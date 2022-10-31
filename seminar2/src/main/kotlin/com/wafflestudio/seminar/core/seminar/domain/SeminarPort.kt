package com.wafflestudio.seminar.core.seminar.domain

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest

interface SeminarPort {
    fun createSeminar(userId: Long, createSeminarRequest: CreateSeminarRequest): CreateSeminarResponse

    fun editSeminar(userId: Long, editSeminarRequest: EditSeminarRequest): EditSeminarResponse
}