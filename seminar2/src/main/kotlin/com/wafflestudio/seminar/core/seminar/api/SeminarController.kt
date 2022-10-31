package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/seminar")
class SeminarController(
    private val seminarService: SeminarService
) {
    @Authenticated
    @PostMapping("/")
    fun makeSeminar(
        @UserContext userId: Long,
        @Valid @RequestBody seminarRequest: SeminarRequest,
    ) = seminarService.makeSeminar(userId, seminarRequest)
    
    @Authenticated
    @PutMapping("/")
    fun editSeminar(
        @UserContext userId: Long,
        @Valid @RequestBody seminarRequest: EditSeminarRequest,
    ) = seminarService.editSeminar(userId, seminarRequest)
    
    @Authenticated
    @GetMapping("/{seminar_id}/")
    fun getSeminar(
        @UserContext userId: Long,
        @PathVariable("seminar_id") seminarId: Long,
    ) = seminarService.getSeminar(userId, seminarId)

    @Authenticated
    @GetMapping("/")
    fun getAllSeminar(
        @UserContext userId: Long,
    ) = seminarService.getAllSeminar(userId)
}