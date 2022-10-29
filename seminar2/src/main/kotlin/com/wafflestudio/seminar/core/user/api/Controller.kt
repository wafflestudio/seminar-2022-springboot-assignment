package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.api.response.*
import com.wafflestudio.seminar.core.user.service.*
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
    fun signup(@RequestBody user: SignUpRequest) : AuthToken {
        
        authService.signup(user)
        // 비밀번호 powerEncoder 추가해야함, 아이디 중복된거 회원가입 못하게 해야함
        // 비밀번호 규칙도 걸어두면 참~ 좋겠네
       
        return authTokenService.generateTokenByEmail(user.email)
    }
    
    @PostMapping("/api/v1/login")
    fun login(@RequestBody userLogin: LoginRequest) : AuthToken {
        
        // 이메일 없으면 오류, 이메일 있지만 비번 틀렸으면 오류
      return  authService.login(userLogin)
    }
    
    @Authenticated
    @GetMapping("/api/v1/user/{user_id}")
    fun getProfile(@PathVariable user_id: Long, @RequestHeader("Authentication") token: String): GetProfile {
        
        return userService.getProfile(user_id,token)

    }
    
    @Authenticated
    @PutMapping("/api/v1/user/me")
    fun updateProfile(@RequestBody userProfile: UpdateProfileRequest, @RequestHeader("Authentication") token: String): GetProfile{
        return userService.updateProfile(userProfile, token)
    }

    @PostMapping("/api/v1/user/participant")
    fun beParticipant(@RequestBody participant: BeParticipantRequest, @RequestHeader("Authentication") token: String): GetProfile{
        return userService.beParticipant(participant, token)
    }
    
    @PostMapping("/api/v1/seminar")
    fun createSeminar(@RequestBody seminar: SeminarRequest, @RequestHeader("Authentication") token: String): SeminarInfo {
        
        return seminarService.createSeminar(seminar, token)
    }

    @PutMapping("/api/v1/seminar")
    fun updateSeminar(@RequestBody seminar: SeminarRequest, @RequestHeader("Authentication") token: String): UpdateSeminarInfo {

        return seminarService.updateSeminar(seminar, token)
    }
    
    @GetMapping("/api/v1/seminar/{seminar_id}")
    fun getSeminarById(@PathVariable seminar_id: Long, @RequestHeader("Authentication") token: String):SeminarInfo{
        return seminarService.getSeminarById(seminar_id,token)
    }
    /*
    @GetMapping("/api/v1/seminar/")
    fun getSeminars(@RequestHeader("Authentication") token: String): List<SeminarInfo>?{
        return seminarService.getSeminars(token)
    }
    
     */
    
    @GetMapping("/api/v1/seminar")
    fun getSeminarByName(@RequestParam name: String, @RequestParam order: String, @RequestHeader("Authentication") token: String): SeminarInfoByName {
        //todo: url 잘못되어 있을 수도?
        return seminarService.getSeminarByName(name, order, token)
    }
    
    
 
 
    @PostMapping("/api/v1/seminar/{seminar_id}/user")
    fun joinSeminar(@PathVariable seminar_id: Long, @RequestHeader("Authentication") token: String): JoinSeminarInfo{
        return seminarService.joinSeminar(seminar_id, token)
    }
    
    
    

}