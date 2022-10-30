package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.LoginUser
import com.wafflestudio.seminar.common.SeminarRequestBodyException
import com.wafflestudio.seminar.core.user.api.dto.SignInRequest
import com.wafflestudio.seminar.core.user.api.dto.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.service.AuthException
import com.wafflestudio.seminar.core.user.service.AuthService
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class AuthController(
        private val authService: AuthService,
        private val userService: UserService
) {
    @PostMapping("/api/v1/signup")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest, bindingResult: BindingResult): AuthToken {
//        ("회원가입을 구현해주세요.")
        if (bindingResult.hasErrors()) {
            throw SeminarRequestBodyException(bindingResult.fieldErrors)
        }
        
        return authService.createUserAndReturnToken(signUpRequest)
    }

    @PostMapping("/api/v1/signin")
    fun logIn(@Valid @RequestBody signInRequest: SignInRequest, bindingResult: BindingResult): Any {
//        TODO("회원가입을 진행한 유저가 로그인할 경우, JWT를 생성해서 내려주세요.")
        if (bindingResult.hasErrors()) {
            throw SeminarRequestBodyException(bindingResult.fieldErrors)
        }
        
        return try {
            authService.findUserAndReturnToken(signInRequest)
        } catch (e: AuthException) {
            return ResponseEntity<String>(e.message, HttpStatus.UNAUTHORIZED)
        }
    }
    
    @GetMapping("/api/v1/me")
    fun getMe(@LoginUser user: UserEntity?): Any {
//        TODO("인증 토큰을 바탕으로 유저 정보를 적당히 처리해서, 본인이 잘 인증되어있음을 알려주세요.")
        user?.let{ return userService.constructUserInformationByUser(it) }
                ?: return ResponseEntity<String>("Unauthorized", HttpStatus.UNAUTHORIZED)
    }
}