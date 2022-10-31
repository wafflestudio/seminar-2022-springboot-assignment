package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.RegRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.SeminarDTO
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
class SeminarController (
    private val seminarService: SeminarService
){
    @Authenticated
    @Transactional
    @PostMapping("/api/v1/seminar")
    fun makeSeminar(
        @RequestHeader("Authorization") accessToken: String,
        @UserContext userId: Long,
        @Valid @RequestBody request: SeminarRequest
    ): SeminarDTO = seminarService.makeSeminar(userId, request)
    
    
    @Authenticated
    @Transactional
    @PutMapping("/api/v1/seminar")
    fun editSeminar(
        @RequestHeader("Authorization") accessToken: String,
        @UserContext userId: Long,
        @Valid @RequestBody request: SeminarDTO,
    ): SeminarDTO = seminarService.editSeminar(userId, request)
    
    
    @Authenticated
    @GetMapping("/api/v1/seminar")
    fun getSeminar(
        @RequestHeader("Authorization") accessToken: String,
        @RequestParam name: String?,
        @RequestParam order: String?,
    ) = seminarService.getSeminar(name, order)
    
    
    @Authenticated
    @GetMapping("/api/v1/seminar/{seminar_id}")
    fun findSeminarById(
        @RequestHeader("Authorization") accessToken: String,
        @PathVariable("seminar_id") seminarId: Long
    ) = seminarService.findSeminarById(seminarId)
    
    
    @Transactional
    @Authenticated
    @PostMapping("/api/v1/seminar/{seminar_id}/user")
    fun regSeminar(
        @RequestHeader("Authorization") accessToken: String,
        @UserContext userId: Long,
        @PathVariable("seminar_id") seminarId: Long,
        @Valid @RequestBody request: RegRequest
    ): Any? = seminarService.regSeminar(userId, seminarId, request)

    
    @Transactional
    @Authenticated
    @DeleteMapping("/api/v1/seminar/{seminar_id}/user")
    fun dropSeminar(
        @RequestHeader("Authorization") accessToken: String,
        @UserContext userId: Long,
        @PathVariable("seminar_id") seminarId: Long,
    ): SeminarDTO = seminarService.dropSeminar(userId, seminarId)
}