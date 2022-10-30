package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.GetSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class SeminarController(
    private val seminarService: SeminarService,
) {
    @Authenticated
    @PostMapping("api/v1/seminar")
    fun createSeminar(
        @UserContext userId: Long,
        @RequestBody request: CreateSeminarRequest
    ): Seminar {
        return seminarService.createSeminar(userId, request)
    }
    
    @Authenticated
    @GetMapping("api/v1/seminar/{seminarId}")
    fun getSeminar(
        @PathVariable seminarId: Long,
    ): Seminar {
        return seminarService.getSeminar(seminarId)
    }
    
    @Authenticated
    @PostMapping("api/v1/seminar/{seminarId}/user")
    fun joinToSeminar(
        @UserContext userId: Long,
        @PathVariable seminarId: Long,
        @RequestBody request: JoinSeminarRequest
    ): Seminar {
        return seminarService.joinToSeminar(userId, seminarId, request.role)
    }
    
    @Authenticated
    @DeleteMapping("api/v1/seminar/{seminarId}/user")
    fun joinToSeminar(
        @UserContext userId: Long,
        @PathVariable seminarId: Long,
    ): Seminar {
        return seminarService.dropSeminar(userId, seminarId)
    }
    
    @Authenticated
    @GetMapping("api/v1/seminar")
    fun getSeminarList(
        request: GetSeminarRequest
    ): List<Seminar> {
        return seminarService.getSeminarList(request)
    }
    
    @Authenticated
    @PutMapping("api/v1/seminar")
    fun createSeminar(
        @UserContext userId: Long,
        @RequestBody request: UpdateSeminarRequest
    ) {
        seminarService.updateSeminar(userId, request)
    }
}