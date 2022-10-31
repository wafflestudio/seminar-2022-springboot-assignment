package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.CreateSeminarResponse
import com.wafflestudio.seminar.core.seminar.domain.EditSeminarResponse
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class SeminarController(
    private val seminarService: SeminarService
) {
    @Authenticated
    @PostMapping("/seminar")
    fun createSeminar(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody @Valid createSeminarRequest: CreateSeminarRequest,
        @UserContext userId: Long
    ): CreateSeminarResponse {
        return seminarService.createSeminar(userId, createSeminarRequest)
    }

    @Authenticated
    @PutMapping("/seminar")
    fun editSeminar(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody @Valid editSeminarRequest: EditSeminarRequest,
        @UserContext userId: Long
    ): EditSeminarResponse {
        return seminarService.editSeminar(userId, editSeminarRequest)
    }
}