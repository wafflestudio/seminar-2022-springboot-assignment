package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AppServiceImpl
import com.wafflestudio.seminar.core.user.service.AuthToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController (private val appService: AppServiceImpl) {
    
    @PostMapping("/api/v1/signup")
    fun signUp(@RequestBody req: SignUpRequest) : AuthToken {
        return appService.signUp(req)
    }
    
    @PostMapping("/api/v1/signin")
    fun logIn(@RequestParam email: String, @RequestParam pwd: String) : AuthToken {
        return appService.logIn(email, pwd)
    }
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(@RequestHeader(value="Authorization") token: String) {
        TODO("인증 토큰을 바탕으로 유저 정보를 적당히 처리해서, 본인이 잘 인증되어있음을 알려주세요.")
    }
    
    
    
}