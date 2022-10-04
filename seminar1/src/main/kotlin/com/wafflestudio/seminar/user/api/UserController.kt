package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.api.request.SurveyRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.wafflestudio.seminar.survey.api.Seminar403
@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService,
    private val seminarService: SeminarService,
){
    @PostMapping("/user")
    fun signup(@RequestBody userInfo:UserEntity):UserEntity{
        return userService.signUp(userInfo);
    }
    
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): Long? {
        return userService.signIn(loginRequest);
    }

    @GetMapping("/user/me")
    fun getInfo(@RequestHeader("X-User-ID") id: Long?): UserEntity? {
        if(id==null)throw Seminar403("id가 전달되지 않았습니다");
        return userService.getUserById(id)
    }
    @PostMapping("/survey")
    fun addSurvey(@RequestHeader("X-User-ID") id: Long,@RequestBody request:SurveyRequest):SurveyResponseEntity{
        if(id==null)throw Seminar403("id가 전달되지 않았습니다");
        return seminarService.addSurvey(request,id);        
    }
}