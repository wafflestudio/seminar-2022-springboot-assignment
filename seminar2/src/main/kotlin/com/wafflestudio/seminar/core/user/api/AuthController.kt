package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.domain.UserInfo
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class AuthController(
    val userService: UserService
) {

    @PostMapping("/api/v1/signup")
    fun signUp(
        @RequestBody @Valid signUpRequest: SignUpRequest
    ): AuthToken {
        return userService.signUp(signUpRequest)
    }

    @PostMapping("/api/v1/signin")
    fun logIn(
        @RequestBody @Valid logInRequest: LogInRequest
    ): AuthToken {
        return userService.logIn(logInRequest)
    }

    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(
        @UserContext userid: Long
    ): UserInfo {
        return userService.getUserById(userid)
    }
    
    @Authenticated
    @GetMapping("/api/v1/user/{userid}")
    fun getUser(
        @PathVariable userid: Long
    ): UserInfo {
        return userService.getUserById(userid)
    }
    
    @Authenticated
    @PutMapping("/api/v1/user/me")
    fun updateUser(
        @UserContext userid: Long,
        @RequestBody @Valid updateRequest: UpdateRequest,
    ): UserInfo {
        return userService.updateUser(userid, updateRequest)
    }
    
    @Authenticated
    @PostMapping("api/v1/user/participant")
    fun participantEnroll(
        @UserContext userid: Long,
        @RequestBody participateSeminarRequest: ParticipantEnrollRequest
    ): UserInfo {
        return userService.participantEnroll(userid, participateSeminarRequest)
    }

}