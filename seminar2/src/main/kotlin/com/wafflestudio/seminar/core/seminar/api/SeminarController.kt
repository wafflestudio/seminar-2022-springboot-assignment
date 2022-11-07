package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.MakeSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.MakeSeminarResponse
import com.wafflestudio.seminar.core.seminar.api.response.QuerySeminarResponses
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AuthService
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SeminarController (
        private val seminarService: SeminarService,
        private val authService: AuthService,
        private val authTokenService: AuthTokenService,
){

    @Authenticated
    @PostMapping("/api/v1/seminar")
    fun makeSeminar(@RequestHeader token: String, @RequestBody request : MakeSeminarRequest) : MakeSeminarResponse {
        val id = authService.getUserId(authTokenService.getCurrentEmail(token))
        return seminarService.makeSeminar(id, request)
    }
    
    @Authenticated
    @PutMapping("/api/v1/seminar")
    fun putSeminar(@RequestHeader token: String, @RequestBody request : MakeSeminarRequest) : MakeSeminarResponse {
        val id = authService.getUserId(authTokenService.getCurrentEmail(token))
        return seminarService.putSeminar(id, request)
    }
    
    @Authenticated
    @GetMapping("/api/v1/seminar/{seminar_id}")
    fun getSeminar(@PathVariable seminar_id: Long) : MakeSeminarResponse {
        return seminarService.getSeminar(seminar_id)
    }
    
    @Authenticated
    @GetMapping("/api/v1/seminar/")
    fun querySeminar(@RequestParam(required = false) name: String?, @RequestParam(required = false) order: String?) : QuerySeminarResponses {
        return seminarService.querySeminar(name, order)
    }
    
    @Authenticated
    @PostMapping("/api/v1/seminar/{seminar_id}/user/")
    fun joinSeminar(@RequestHeader token: String, @PathVariable seminar_id: Long, @RequestBody request: JoinSeminarRequest) : MakeSeminarResponse {
        val id = authService.getUserId(authTokenService.getCurrentEmail(token))
        return seminarService.joinSeminar(id, request, seminar_id)
    }
    
    @Authenticated
    @DeleteMapping("/api/v1/seminar/{seminar_id}/user/")
    fun deleteSeminar(@RequestHeader token: String, @PathVariable seminar_id: Long) {
        val id = authService.getUserId(authTokenService.getCurrentEmail(token))
        seminarService.deleteSeminar(id, seminar_id)
    }
}