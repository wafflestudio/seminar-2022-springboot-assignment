package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.seminar.api.request.ApplySeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.ModifySeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.seminar.domain.SeminarForList

interface SeminarService {
    fun createSeminar(authToken: String, createSeminarRequest: CreateSeminarRequest): Seminar
    fun modifySeminar(authToken: String, modifySeminarRequest: ModifySeminarRequest): Seminar
    fun getAllSeminar(seminarName: String?, order: String?):List<SeminarForList>
    fun readSeminar(seminarId: Long): Seminar
    fun applySeminar(authToken: String, seminarId: Long, applySeminarRequest: ApplySeminarRequest): Seminar
    fun deleteParticipantFromSeminar(authToken: String, seminarId: Long): String
}