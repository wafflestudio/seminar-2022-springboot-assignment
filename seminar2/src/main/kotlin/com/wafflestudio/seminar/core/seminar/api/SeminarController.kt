package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.SearchSeminarResponse
import com.wafflestudio.seminar.core.seminar.domain.SeminarResponse
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
    ): SeminarResponse {
        return seminarService.createSeminar(userId, createSeminarRequest)
    }

    @Authenticated
    @PutMapping("/seminar")
    fun editSeminar(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody @Valid editSeminarRequest: EditSeminarRequest,
        @UserContext userId: Long
    ): SeminarResponse {
        return seminarService.editSeminar(userId, editSeminarRequest)
    }

    @Authenticated
    @GetMapping("/seminar/{seminar_id}")
    fun getSeminar(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable seminar_id: Long
    ): SeminarResponse {
        return seminarService.getSeminar(seminar_id)
    }

    @Authenticated
    @GetMapping("/seminar")
    fun searchSeminar(
        @RequestHeader("Authorization") authHeader: String,
        @RequestParam name: String?,
        @RequestParam order: String?
    ): List<SearchSeminarResponse> {
        return seminarService.searchSeminar(name = name, order = order)
    }
}