package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.user.api.request.AuthUserRequest
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1")
class UserController (private val service: UserService) {

    @PostMapping("/user")
    fun create(@RequestBody request: CreateUserRequest): User {
        return service.create(request)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginUserRequest): User {
        return service.login(request)
    }
    
    @GetMapping("/user/me")
    fun inquiry(@RequestHeader("X-User-Id") id: Long?) : UserEntity? {
        println(id)
        return service.inquiry(id)
    }

    @PostMapping("/survey")
    fun postSurvey(
        @RequestHeader("X-User-Id") userId : Long?,
        @RequestBody request: CreateSurveyRequest,
    ): Long? { userId ?: throw Seminar404("로그인을 해주세요.")
        return service.surveyCreate(userId, request)
    }
}