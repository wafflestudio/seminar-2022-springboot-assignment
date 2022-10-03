package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.survey.service.UserService
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController 
@RequestMapping("/api/v1")
class UserController(
    private val service: UserService
) {
    @RequestMapping(path=["/user"], method=[RequestMethod.POST])
    fun register(@RequestBody userdata: CreateUserRequest)=service.register(userdata)
    
    @RequestMapping(path=["/login"], method = [RequestMethod.POST])
    fun login(@RequestBody userdata: LoginUserRequest)=service.login(userdata)

    @RequestMapping(path=["/user/me"], method = [RequestMethod.GET])
    fun getById(@RequestHeader("X-User-ID") userid: Long )=service.getById(userid)
}
