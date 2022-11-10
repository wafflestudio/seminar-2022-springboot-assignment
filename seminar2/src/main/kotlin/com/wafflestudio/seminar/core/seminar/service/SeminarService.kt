package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.domain.Role

interface SeminarService {
    fun createSeminar(userId: Long, createSeminarRequest: CreateSeminarRequest): Seminar
    fun editSeminar(userId: Long, editSeminarRequest: EditSeminarRequest): Seminar
    fun getSeminar(seminarId: Long): Seminar
    fun getSeminars(name: String, order: String): List<SeminarResponse>
    fun joinSeminar(userId: Long, seminarId: Long, role: Role): Seminar
    fun dropSeminar(userId: Long, seminarId: Long): Seminar
}