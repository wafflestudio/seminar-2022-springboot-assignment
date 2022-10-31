package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.SeminarDto
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class SeminarController(
    private val seminarService: SeminarService
) {
    @Authenticated
    @PostMapping("/api/v1/seminar/")
    fun makeSeminar(
        @RequestHeader(value = "Authorization") authorization: String,
        @Valid @RequestBody req: SeminarDto.SeminarRequest,
        @UserContext userId: Long
    ): ResponseEntity<SeminarDto.SeminarProfileResponse> =
        ResponseEntity(seminarService.makeSeminar(userId, req), HttpStatus.OK)

    @Authenticated
    @PutMapping("/api/v1/seminar")
    fun updateSeminar(
        @RequestHeader(value = "Authorization") authorization: String,
        @Valid @RequestBody req: SeminarDto.UpdateSeminarRequest,
        @UserContext userId: Long
    ): ResponseEntity<SeminarDto.SeminarProfileResponse> =
        ResponseEntity(seminarService.updateSeminar(userId, req), HttpStatus.OK)
}