package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.api.response.*
import com.wafflestudio.seminar.core.user.service.*
import org.springframework.lang.Nullable
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
    fun getProfile(@PathVariable user_id: Long, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetProfile {
        
        return userService.getProfile(user_id,userId)

    }
    
    @Authenticated
    @PutMapping("user/me")
    fun updateProfile(@RequestBody userProfile: UpdateProfileRequest, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetProfile{
        return userService.updateProfile(userProfile, userId)
    }

    @Authenticated
    @PostMapping("user/participant")
    fun beParticipant(@RequestBody participant: RegisterParticipantRequest, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetProfile{
        return userService.registerParticipant(participant, userId)
    }
    
    @Authenticated
    @PostMapping("seminar")
    fun createSeminar(@RequestBody seminar: SeminarRequest, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetSeminarInfo {
        
        return seminarService.createSeminar(seminar, userId)
    }

    @Authenticated
    @PutMapping("seminar")
    fun updateSeminar(@RequestBody seminar: SeminarRequest, @RequestHeader("Authorization") token: String, @UserContext userId: Long): UpdateSeminarInfo {

        return seminarService.updateSeminar(seminar, userId)
    }
    
    @GetMapping("seminar/{seminar_id}")
    fun getSeminarById(@PathVariable seminar_id: Long):GetSeminarInfo{
        return seminarService.getSeminarById(seminar_id)
    }
    

    @GetMapping("seminar")
    fun getSeminarList(@RequestParam @Nullable name: String?, @RequestParam @Nullable order: String?): List<GetSeminarInfo>{
        return seminarService.getSeminarList(name, order)
    }


    @Authenticated
    @PostMapping("seminar/{seminar_id}/user")
    fun joinSeminar(@PathVariable seminar_id: Long, @RequestBody role: Map<String,String>, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetSeminarInfo{
        
        return seminarService.joinSeminar(seminar_id,role, userId)
    }

    @Authenticated
    @DeleteMapping("seminar/{seminar_id}/user")
    fun dropSeminar(@PathVariable seminar_id: Long,@RequestHeader("Authorization") token: String, @UserContext userId: Long): GetSeminarInfo {
        return seminarService.dropSeminar(seminar_id,userId)
        
    }
    
}