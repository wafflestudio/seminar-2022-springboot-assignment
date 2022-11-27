package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.seminar.domain.Seminar
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
        @UserContext userId: Long,
        @Valid @RequestBody createSeminarRequest: CreateSeminarRequest
    ): Seminar {
        return seminarService.createSeminar(userId, createSeminarRequest)
    }

    @Authenticated
    @PutMapping("/seminar")
    fun editSeminar(@UserContext userId: Long, @Valid @RequestBody editSeminarRequest: EditSeminarRequest): Seminar {
        return seminarService.editSeminar(userId, editSeminarRequest)
    }

    @Authenticated
    @GetMapping("/seminar/{seminarId}")
    fun getSeminar(@PathVariable("seminarId") seminarId: Long): Seminar {
        return seminarService.getSeminar(seminarId)
    }

    @Authenticated
    @GetMapping("/seminar")
    fun getSeminars(
        @RequestParam(name = "name", defaultValue = "") name: String,
        @RequestParam(name = "order", defaultValue = "") order: String
    ): List<SeminarResponse> {
        return seminarService.getSeminars(name, order)
    }

    @Authenticated
    @PostMapping("/seminar/{seminarId}/user")
    fun joinSeminar(
        @UserContext userId: Long,
        @PathVariable("seminarId") seminarId: Long,
        @RequestBody joinSeminarRequest: JoinSeminarRequest
    ): Seminar {
        if (joinSeminarRequest.role == null) {
            throw Seminar400("role is required")
        }
        return seminarService.joinSeminar(userId, seminarId, joinSeminarRequest.role)
    }

    @Authenticated
    @DeleteMapping("/seminar/{seminarId}/user")
    fun dropSeminar(@UserContext userId: Long, @PathVariable("seminarId") seminarId: Long): Seminar {
        return seminarService.dropSeminar(userId, seminarId)
    }

}