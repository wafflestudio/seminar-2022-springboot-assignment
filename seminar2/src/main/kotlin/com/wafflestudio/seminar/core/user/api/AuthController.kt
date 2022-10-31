package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.EditProfileRequest
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.response.ProfileResponse
import com.wafflestudio.seminar.core.user.service.AuthService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
class AuthController (
    val authService: AuthService
        ) {
    
    @PostMapping("/api/v1/signup")
    fun signUp(@RequestBody signUpRequest: SignUpRequest): String {
        return authService.signUp(signUpRequest)
    }
    
    @PostMapping("/api/v1/signin")
    fun logIn(@RequestBody logInRequest: LoginRequest): String {
        return authService.login(logInRequest)
    }
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(request: HttpServletRequest) {
        TODO("인증 토큰을 바탕으로 유저 정보를 적당히 처리해서, 본인이 잘 인증되어있음을 알려주세요.")
    }
    
    @Authenticated
    @GetMapping("/api/v1/user/{user_id}")
    fun getProfile(@PathVariable user_id: Long, request:HttpServletRequest): ProfileResponse{
        return authService.getProfile(user_id, request)
    }
    
    @Authenticated
    @PutMapping("/api/v1/user/me")
    fun editProfile(request: HttpServletRequest, @RequestBody editProfileRequest: EditProfileRequest) : ProfileResponse{
        return authService.editProfile(request, editProfileRequest)
        
    }
}