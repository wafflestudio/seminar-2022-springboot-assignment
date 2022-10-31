package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.api.request.ParticipateRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SeminarController (
    val seminarService : SeminarService
        ){
    @Authenticated
    @PostMapping("/api/v1/seminar")
    fun createSeminar(
        @RequestBody request : CreateSeminarRequest,
        @UserContext userID : Long?
    ){
        
        if(userID != null){
            seminarService.create(request, userID)

        }

    }
    
    @Authenticated
    @PutMapping("/api/v1/seminar")
    fun updateSeminar(
        @RequestBody request : UpdateSeminarRequest,
        @UserContext userID : Long?
    ){
        
        if(userID != null){
            seminarService.update(request, userID)
        }
    }
    
    @Authenticated
    @GetMapping("/api/v1/seminar/{seminar_id}")
    fun getSeminar(
        @PathVariable seminarId : Long,
    ){
        seminarService.get(seminarId)
    }
    
    
    @Authenticated
    @GetMapping("/api/v1/seminar")
    fun getSeminarsByQuery(
        @RequestParam name : String?,
        @RequestParam order : String?
    ){
        if(name == null && order != "earliest"){
            seminarService.getAll()
        }else if(name == null && order == "earliest"){
            seminarService.getAllDesc()
        }
        else if(name != null && order != "earliest"){
            seminarService.getByName(name)
        }
        else if(name != null && order == "earliest"){
            seminarService.getByNameDesc(name)
        }
        else{
            throw Seminar400("잘못돤 명령을 입력했습니다.")
        }
    }
    
}