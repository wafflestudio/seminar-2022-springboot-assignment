package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipateRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(private val authTokenService: AuthTokenService, private val userService: UserService) {
    
    @PostMapping("/api/v1/signup")
    fun signUp(@RequestBody request : SignUpRequest) : String{
        userService.join(request)
        return authTokenService.generateTokenByUsername(request.email).accessToken
    }
    
    @PostMapping("/api/v1/signin")
    fun logIn(@RequestBody request : LoginRequest):String {
        userService.login(request.email, request.password)
        return authTokenService.generateTokenByUsername(request.email).accessToken
    }
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(
        @UserContext userID : Long?
    ):String {
        return "User : ${userID.toString()}" 
    }
    @GetMapping("/total")
    fun findTotal() : String{
        return userService.findAll().toString()
    }
    @GetMapping("/api/v1/user/{user_id}")
    fun loadProfile(@PathVariable user_id : Long)
    = userService.loadProfile(user_id)
    
    @PutMapping("/api/v1/user/me")
    fun updateMe(
        @UserContext userID : Long?
    ) : String{
        if(userID != null){
            val profile = userService.loadProfile(userID)
            return profile.toString()
            
        }
        
        
        return "업데이트 완료"
    }
    @Authenticated
    @PostMapping("/api/v1/user/participant")
    fun participate(
        @RequestBody request : ParticipateRequest,
        @UserContext userID : Long?
    ){
        if(userID != null){
            userService.participate(userID,request)
        }
        
    }
}