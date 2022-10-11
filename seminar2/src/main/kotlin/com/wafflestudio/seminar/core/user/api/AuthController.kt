package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.domain.UserLogin
import com.wafflestudio.seminar.core.user.service.AuthService
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private var authService: AuthService,
    private var authTokenService: AuthTokenService
) {
    
    @PostMapping("/api/v1/signup")
    fun signUp(@RequestBody user: User) : AuthToken {
        
        authService.save(user)
        // 비밀번호 powerEncoder 추가해야함, 아이디 중복된거 회원가입 못하게 해야함
        // 비밀번호 규칙도 걸어두면 참~ 좋겠네
        return authTokenService.generateTokenByUsername(user.username)
    }
    
    
    @PostMapping("/api/v1/signin")
    fun login(@RequestBody userLogin: UserLogin) : AuthToken {
        
       authService.login(userLogin)
        return authTokenService.generateTokenByUsername(userLogin.username)
    }
    
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe() {
        TODO("인증 토큰을 바탕으로 유저 정보를 적당히 처리해서, 본인이 잘 인증되어있음을 알려주세요.")
    }
    
}