package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.domain.UserSignup
import com.wafflestudio.seminar.core.user.domain.UserLogin
import com.wafflestudio.seminar.core.user.domain.UserProfile
import com.wafflestudio.seminar.core.user.service.AuthService
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class AuthController(
    // valid도 추가하기
    private var authService: AuthService,
    private var authTokenService: AuthTokenService,
    private var userService: UserService
) {
    
    @PostMapping("/api/v1/signup")
    fun signup(@RequestBody user: UserSignup) : AuthToken {
        
        authService.signup(user)
        // 비밀번호 powerEncoder 추가해야함, 아이디 중복된거 회원가입 못하게 해야함
        // 비밀번호 규칙도 걸어두면 참~ 좋겠네
       
        return authTokenService.generateTokenByEmail(user.email)
    }
    
    
    @PostMapping("/api/v1/signin")
    fun login(@RequestBody userLogin: UserLogin) : AuthToken {
        
        // 이메일 없으면 오류, 이메일 있지만 비번 틀렸으면 오류
       authService.login(userLogin)
        return authTokenService.generateTokenByEmail(userLogin.email)
    }
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(@RequestHeader("Authentication") token: String) : String  {
        authTokenService.getCurrentUserId(token)
        
        return "인증되었습니다"
    }
    
    @Authenticated
    @GetMapping("/api/v1/user/{email}")
    fun getProfile(@PathVariable email: String, @RequestHeader("Authentication") token: String): UserProfile{
        return userService.getProfile(email, token)
    }
    
    
}