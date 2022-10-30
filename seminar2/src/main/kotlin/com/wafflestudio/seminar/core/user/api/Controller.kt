package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.api.response.*
import com.wafflestudio.seminar.core.user.service.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class Controller(
    // valid도 추가하기
    private var authService: AuthService,
    private var authTokenService: AuthTokenService,
    private var userService: UserService,
    private var seminarService: SeminarService
) {
    
    @PostMapping("signup")
    fun signup(@RequestBody user: SignUpRequest) : AuthToken {
        
        authService.signup(user)
        // 비밀번호 powerEncoder 추가해야함, 아이디 중복된거 회원가입 못하게 해야함
        // 비밀번호 규칙도 걸어두면 참~ 좋겠네
       
        return authTokenService.generateTokenByEmail(user.email)
    }
    
    @PostMapping("login")
    fun login(@RequestBody userLogin: LoginRequest) : AuthToken {
        
        // 이메일 없으면 오류, 이메일 있지만 비번 틀렸으면 오류
      return  authService.login(userLogin)
    }
    
    @Authenticated
    @GetMapping("user/{user_id}")
    fun getProfile(@PathVariable user_id: Long, @RequestHeader("Authentication") token: String): GetProfile {
        
        return userService.getProfile(user_id,token)

    }
    
    @Authenticated
    @PutMapping("user/me")
    fun updateProfile(@RequestBody userProfile: UpdateProfileRequest, @RequestHeader("Authentication") token: String): GetProfile{
        return userService.updateProfile(userProfile, token)
    }

    @PostMapping("user/participant")
    fun beParticipant(@RequestBody participant: BeParticipantRequest, @RequestHeader("Authentication") token: String): GetProfile{
        return userService.beParticipant(participant, token)
    }
    
    @PostMapping("seminar")
    fun createSeminar(@RequestBody seminar: SeminarRequest, @RequestHeader("Authentication") token: String): SeminarInfo {
        
        return seminarService.createSeminar(seminar, token)
    }

    @PutMapping("seminar")
    fun updateSeminar(@RequestBody seminar: SeminarRequest, @RequestHeader("Authentication") token: String): UpdateSeminarInfo {

        return seminarService.updateSeminar(seminar, token)
    }
    
    @GetMapping("seminar/{seminar_id}")
    fun getSeminarById(@PathVariable seminar_id: Long, @RequestHeader("Authentication") token: String):SeminarInfo{
        return seminarService.getSeminarById(seminar_id,token)
    }
    /*
    @GetMapping("/api/v1/seminar/")
    fun getSeminars(@RequestHeader("Authentication") token: String): List<SeminarInfo>?{
        return seminarService.getSeminars(token)
    }
    
     */
    
    @GetMapping("seminar")
    fun getSeminarByName(@RequestParam name: String, @RequestParam order: String, @RequestHeader("Authentication") token: String): SeminarInfoByName {
        //todo: url 잘못되어 있을 수도?
        return seminarService.getSeminarByName(name, order, token)
    }
    
    @PostMapping("seminar/{seminar_id}/user")
    fun joinSeminar(@PathVariable seminar_id: Long, @RequestBody role: Map<String,String>, @RequestHeader("Authentication") token: String): JoinSeminarInfo{
        
        return seminarService.joinSeminar(seminar_id,role, token)
    }
    
    @DeleteMapping("seminar/{seminar_id}/user")
    fun dropSeminar(@PathVariable seminar_id: Long,): String {
        seminarService.dropSeminar(seminar_id)
        return "1"
    }
    
    

}