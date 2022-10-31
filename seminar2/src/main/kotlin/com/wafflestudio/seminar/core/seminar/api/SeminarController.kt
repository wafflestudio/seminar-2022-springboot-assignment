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

    @Authenticated
    @GetMapping("/api/v1/seminar/{seminar_id}/")
    fun getSeminarById(
        @PathVariable seminar_id: Long,
        @RequestHeader(value = "Authorization") authorization: String,
    ): ResponseEntity<SeminarDto.SeminarProfileResponse> =
        ResponseEntity(seminarService.getSeminarById(seminar_id), HttpStatus.OK)

    @Authenticated
    @GetMapping("/api/v1/seminar/")
    fun getSeminars(
        @RequestHeader(value = "Authorization") authorization: String,
        @RequestParam(name = "name", required = false) name: String?,
        @RequestParam(name = "order", required = false) earliest: String?
    ): ResponseEntity<MutableList<SeminarDto.SeminarProfileSimplifiedResponse>> =
        ResponseEntity(seminarService.getSeminars(name, earliest), HttpStatus.OK)

    @Authenticated
    @PostMapping("/api/v1/seminar/{seminar_id}/user/")
    fun participateSeminar(
        @RequestHeader(value = "Authorization") authorization: String,
        @PathVariable seminar_id: Long,
        @RequestBody role: String,
        @UserContext userId: Long
    ): ResponseEntity<SeminarDto.SeminarProfileResponse> =
        ResponseEntity(seminarService.participateSeminar(seminar_id, role, userId), HttpStatus.OK)
}