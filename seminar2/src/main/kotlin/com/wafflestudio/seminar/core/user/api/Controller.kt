package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.api.response.*
import com.wafflestudio.seminar.core.user.service.*
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class Controller(

    private var authService: AuthService,
    private var authTokenService: AuthTokenService,
    private var userService: UserService,
    private var seminarService: SeminarService
) {
    
    @PostMapping("signup")
    fun signup(@RequestBody user: SignUpRequest) : AuthToken {
        
        authService.signup(user)

       
        return authTokenService.generateTokenByEmail(user.email)
    }
    
    @PostMapping("login")
    fun login(@RequestBody userLogin: LoginRequest) : AuthToken {
        
      return  authService.login(userLogin)
    }
    
    @Authenticated
    @GetMapping("user/{user_id}")
    fun getProfile(@PathVariable user_id: Long, @RequestHeader("Authorization") token: String): GetProfile {
        
        return userService.getProfile(user_id,token)

    }
    
    @Authenticated
    @PutMapping("user/me")
    fun updateProfile(@RequestBody userProfile: UpdateProfileRequest, @RequestHeader("Authorization") token: String): GetProfile{
        return userService.updateProfile(userProfile, token)
    }

    @PostMapping("user/participant")
    fun beParticipant(@RequestBody participant: BeParticipantRequest, @RequestHeader("Authorization") token: String): GetProfile{
        return userService.beParticipant(participant, token)
    }
    
    @PostMapping("seminar")
    fun createSeminar(@RequestBody seminar: SeminarRequest, @RequestHeader("Authorization") token: String): GetSeminarInfo {
        
        return seminarService.createSeminar(seminar, token)
    }

    @PutMapping("seminar")
    fun updateSeminar(@RequestBody seminar: SeminarRequest, @RequestHeader("Authorization") token: String): UpdateSeminarInfo {

        return seminarService.updateSeminar(seminar, token)
    }
    
    @GetMapping("seminar/{seminar_id}")
    fun getSeminarById(@PathVariable seminar_id: Long, @RequestHeader("Authorization") token: String):GetSeminarInfo{
        return seminarService.getSeminarById(seminar_id,token)
    }
    

    /*
    * TODO: 해당하는 api가 없습니다.
    *       seminars -> seminar로 수정되어야 합니다.
    */
    @GetMapping("seminars")
    fun getSeminars(@RequestHeader("Authorization") token: String): List<GetSeminars>{
        return seminarService.getSeminars(token)
    }

    /*
    * TODO: name과 order는 없을 수도 있기 때문에 nullable하게 선언되어야 합니다.
    *       또한 스펙에 따르면 return값은 GetSeminarInfoByName이 아니라 List<GetSeminarInfoByName>이어야 합니다.
    */
    @GetMapping("seminar")
    fun getSeminarByName(@RequestParam name: String, @RequestParam order: String, @RequestHeader("Authorization") token: String): GetSeminarInfoByName {
        return seminarService.getSeminarByName(name, order, token)
    }
    
    @PostMapping("seminar/{seminar_id}/user")
    fun joinSeminar(@PathVariable seminar_id: Long, @RequestBody role: Map<String,String>, @RequestHeader("Authorization") token: String): JoinSeminarInfo{
        
        return seminarService.joinSeminar(seminar_id,role, token)
    }
    
    @DeleteMapping("seminar/{seminar_id}/user")
    fun dropSeminar(@PathVariable seminar_id: Long,@RequestHeader("Authorization") token: String): GetSeminarInfo {
        return seminarService.dropSeminar(seminar_id,token)
        
    }
    
     
    
    

}