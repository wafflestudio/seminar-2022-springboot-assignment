package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.seminar.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.seminar.api.request.createSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.CreateSeminarResponse
import com.wafflestudio.seminar.core.seminar.api.response.QuerySeminarResponse
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.api.request.RegisterSeminarRequest
import com.wafflestudio.seminar.core.user.api.response.ProfileResponse
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
class SeminarController (
    val seminarService: SeminarService
        ){
    
    @Authenticated
    @PostMapping("api/v1/seminar")
    fun createSeminar(request: HttpServletRequest, @RequestBody createSeminarRequest: createSeminarRequest): CreateSeminarResponse {
        return seminarService.createSeminar(request, createSeminarRequest)
    } 
    
    @Authenticated
    @GetMapping("api/v1/seminar/{seminar_id}")
    fun getSeminar(request: HttpServletRequest, @PathVariable seminar_id: Long) : CreateSeminarResponse{
        return seminarService.getSeminar(request, seminar_id)
    }
    
    @Authenticated
    @GetMapping("api/v1/seminar")
    fun getQuerySeminar(request: HttpServletRequest, @RequestParam(defaultValue = "") order :String,
    @RequestParam(defaultValue = "") name: String ) : List<QuerySeminarResponse>{
        return seminarService.getQuerySeminar(request, order, name)
    }
    
    @Authenticated
    @PutMapping("api/v1/seminar")
    fun editSeminar(request: HttpServletRequest, @RequestBody createSeminarRequest: createSeminarRequest) : CreateSeminarResponse{
        return seminarService.editSeminar(request, createSeminarRequest)
    }
    
    @Authenticated
    @PostMapping("api/v1/user/participant")
    fun registerToParticipant(request: HttpServletRequest, @RequestBody registerParticipantRequest: RegisterParticipantRequest) : ProfileResponse{
        return seminarService.registerToParticipant(request, registerParticipantRequest)
    }

    @Authenticated
    @PostMapping("/api/v1/seminar/{seminar_id}/user")
    fun registerSeminar(request: HttpServletRequest, @PathVariable seminar_id: Long, @RequestBody registerSeminarRequest: RegisterSeminarRequest): CreateSeminarResponse{
        return seminarService.registerSeminar(request, seminar_id, registerSeminarRequest)
    }
    
    @Authenticated
    @DeleteMapping("/api/v1/seminar/{seminar_id}/user")
    fun dropSeminar(request: HttpServletRequest, @PathVariable seminar_id: Long) : CreateSeminarResponse{
        return seminarService.dropSeminar(request, seminar_id)
    }
}