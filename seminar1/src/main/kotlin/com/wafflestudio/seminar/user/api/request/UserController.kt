package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.domain.UserEntity
import com.wafflestudio.seminar.user.dto.CreateUserDTO
import com.wafflestudio.seminar.user.dto.LoginUserDTO
import com.wafflestudio.seminar.user.exception.User403
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/api/v1/user")
    @ResponseBody
    fun createUser(
        @RequestBody createUserDTO: CreateUserDTO
    ): UserEntity {
        return userService.createUser(createUserDTO)
    }

    @PostMapping("/api/v1/login")
    fun login(
        @RequestBody loginUserDTO: LoginUserDTO
    ): Long{
        return userService.login(loginUserDTO)
    }

    @GetMapping("/api/v1/user/me")
    @ResponseBody
    fun getUser(
        @RequestHeader("X-User-ID") id: Long?
    ): UserEntity {
        id ?: throw User403("X-User-ID 헤더 값이 없습니다.")
        return userService.getUser(id)
    }
    
    @PostMapping("/api/v1/survey")
    @ResponseBody
    fun postSurvey(
        @RequestHeader("X-User-ID") id: Long?,
        @RequestBody @Valid surveyResponseEntity: SurveyResponseEntity
    ):SurveyResponseEntity{
        id ?: throw User403("X-User-ID 헤더 값이 없습니다.")
        return userService.postSurvey(id, surveyResponseEntity)
    }
}