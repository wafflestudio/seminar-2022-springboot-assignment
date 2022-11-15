package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarMakeRequest
import com.wafflestudio.seminar.core.seminar.api.response.CountSeminarResponse
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.seminar.repository.Seminar
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.repository.UserEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class SeminarController(
    private val seminarService: SeminarService,
) {
    
    @Authenticated
    @PostMapping("/api/v1/seminar")
    fun makeSeminar(@UserContext loginUser: UserEntity,
                    @Valid @RequestBody request: SeminarMakeRequest): SeminarResponse
    {
        return seminarService.makeSeminar(loginUser, request)
    }
    
    @Authenticated
    @PutMapping("/api/v1/seminar")
    fun editSeminar(@UserContext loginUser: UserEntity, 
                    @Valid @RequestBody request: EditSeminarRequest): SeminarResponse
    {
        return seminarService.editSeminar(loginUser, request)
    }
    
    @Authenticated
    @GetMapping("/api/v1/seminar/{seminar_id}")
    fun getSeminarById(@PathVariable("seminar_id") seminarId: Long): SeminarResponse {
        return seminarService.findSeminarById(seminarId)
    }
    
    @Authenticated
    @GetMapping("/api/v1/seminar")
    fun getSeminarByQuery(@RequestParam(defaultValue = "") name: String,
                          @RequestParam(defaultValue = "") order: String): List<CountSeminarResponse>
    {
        return seminarService.findSeminarByQuery(name, order)
    }
    
    @Authenticated
    @PostMapping("/api/v1/seminar/{seminar_id}/user")
    fun joinSeminar(@UserContext loginUser: UserEntity,
                    @Valid @RequestBody request: JoinSeminarRequest,
                    @PathVariable("seminar_id") seminarId: Long): SeminarResponse
    {
        return seminarService.joinSeminar(loginUser, request.role, seminarId)
    }

    @Authenticated
    @DeleteMapping("/api/v1/seminar/{seminar_id}/user")
    fun dropSeminar(@UserContext loginUser: UserEntity,
                    @PathVariable("seminar_id") seminarId: Long): SeminarResponse 
    {
        return seminarService.dropSeminar(loginUser, seminarId)
    }
}