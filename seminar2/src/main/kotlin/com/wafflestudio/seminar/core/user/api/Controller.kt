package com.wafflestudio.seminar.core.user.api

import com.querydsl.core.Tuple
import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.dto.ParticipantProfileDto
import com.wafflestudio.seminar.core.user.dto.UserProfileDto
import com.wafflestudio.seminar.core.user.service.*
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*

@RestController
class Controller(
    // valid도 추가하기
    private var authService: AuthService,
    private var authTokenService: AuthTokenService,
    private var userService: UserService,
    private var seminarService: SeminarService
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
    fun getProfile(@PathVariable email: String, @RequestHeader("Authentication") token: String): List<UserProfileDto>{
        return userService.getProfile(email, token)
    }
    
    @Authenticated
    @PutMapping("/api/v1/user/me")
    fun updateMe(@RequestBody userProfile: UserProfile, @RequestHeader("Authentication") token: String): UserProfile{
        return userService.updateMe(userProfile, token)
    }

    @PostMapping("/api/v1/seminar")
    fun createSeminar(@RequestBody seminar: Seminar, @RequestHeader("Authentication") token: String): List<SeminarEntity>{
        
        return seminarService.createSeminar(seminar, token)
    }


    @GetMapping("/api/v1/seminar/{seminarId}")
    fun getSeminarById(@PathVariable seminarId: Long):List<Any>{
        return seminarService.getSeminarById(seminarId)
    }
    @PostMapping("/api/v1/seminar/{seminarId}/user")
    fun joinSeminar(@PathVariable seminarId: Long, @RequestHeader("Authentication") token: String): String{
        return seminarService.joinSeminar(seminarId, token)
    }
    
    
    @GetMapping("/api/v1/profile")
    fun profile(@RequestHeader("Authentication") token: String): List<QueryProjection> {
        return seminarService.profile(token)
    }
    
    @PostMapping("/api/v1/makeProfile/{email}")
    fun makeProfile(@PathVariable email: String): List<ParticipantProfileDto>{
        return userService.makeParticipantProfileDto(email)
    }
    
}