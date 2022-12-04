package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.domain.UserLogin
import com.wafflestudio.seminar.user.domain.UserSurvey
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService,
    private val seminarService: SeminarService
) {

    @PostMapping("/user")
    fun postUser(@RequestBody user: UserEntity): String {
        return userService.register(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody user: UserLogin): String {
        return "유저 아이디는 " + userService.login(user) + " 입니다."
    }

    @GetMapping("/user/me")
    fun getUser(@RequestHeader("X-User-ID") user: Long?): UserEntity?{
        if(user == null) {
            throw Seminar403("로그인이 필요합니다.")
        }else{
            return userService.findMe(user)
        }
    }
}