package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.service.SeminarServiceImpl
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.web.bind.annotation.*
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest as EditSeminarRequest

@RestController
class SeminarController (
    private val seminarService: SeminarServiceImpl,
    private val authTokenService: AuthTokenService
) {

    @Authenticated
    @PostMapping("/api/v1/seminar")
    fun makeSeminar(@RequestHeader(value="Authorization") token: String, @RequestBody req: SeminarRequest) : SeminarResponse {
        return seminarService.makeSeminar(req, authTokenService.getCurrentUserId(token))
    }
    
    @Authenticated
    @PutMapping("/api/v1/seminar/{seminar_id}")
    fun editSeminar(@RequestHeader(value="Authorization") token: String, @PathVariable seminar_id : Long, @RequestBody req: EditSeminarRequest) : SeminarResponse {
        return seminarService.editSeminar(seminar_id, req, authTokenService.getCurrentUserId(token))
    }
    
    @Authenticated
    @GetMapping("/api/v1/seminar/{seminar_id}")
    fun getSeminar(@PathVariable seminar_id : Long) : SeminarResponse {
        return seminarService.getSeminar(seminar_id)
    }

    @Authenticated
    @GetMapping("/api/v1/seminar")
    fun getAllSeminar() : List<SeminarResponse> {
        return seminarService.getAllSeminar()
    }
    
    @Authenticated
    @PostMapping("/api/v1/seminar/{seminar_id}/user")
    fun joinSeminar(@RequestHeader(value="Authorization") token: String, @PathVariable seminar_id : Long, @RequestBody req: JoinSeminarRequest) : SeminarResponse {
        return seminarService.joinSeminar(seminar_id, authTokenService.getCurrentUserId(token), req)
    }
    
    @Authenticated
    @DeleteMapping("/api/v1/seminar/{seminar_id}/user")
    fun dropSeminar(@RequestHeader(value="Authorization") token: String, @PathVariable seminar_id : Long) : SeminarResponse {
        return seminarService.dropSeminar(seminar_id, authTokenService.getCurrentUserId(token))
    }


}