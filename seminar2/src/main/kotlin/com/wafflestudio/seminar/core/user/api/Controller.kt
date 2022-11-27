package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.api.response.GetProfile
import com.wafflestudio.seminar.core.user.api.response.GetSeminarInfo
import com.wafflestudio.seminar.core.user.api.response.UpdateSeminarInfo
import com.wafflestudio.seminar.core.user.service.AuthService
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.SeminarService
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.lang.Nullable
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1")
class Controller(
        private var authService: AuthService,
        private var userService: UserService,
        private var seminarService: SeminarService,
) { 
    
    @PostMapping("signup")
    fun signup(@RequestBody @Valid request: SignUpRequest): AuthToken {
        return authService.signup(request)
    }

    @PostMapping("login")
    fun login(@RequestBody userLogin: LoginRequest): AuthToken {

        return authService.login(userLogin)
    }
    
    @Authenticated
    @GetMapping("user/{user_id}")
    fun getProfile(@PathVariable user_id: Long, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetProfile {
        return userService.getProfile(user_id, userId)

    }

    @Authenticated
    @PutMapping("user/me")
    fun updateProfile(@RequestBody request: UpdateProfileRequest, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetProfile {
        return userService.updateProfile(request, userId)
    }

    @Authenticated
    @PostMapping("user/participant")
    fun beParticipant(@RequestBody request: RegisterParticipantRequest, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetProfile {
        return userService.registerParticipant(request, userId)
    }

    @Authenticated
    @PostMapping("seminar")
    fun createSeminar(@RequestBody @Valid request: SeminarRequest, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetSeminarInfo {
        return seminarService.createSeminar(request, userId)
    }

    @Authenticated
    @PutMapping("seminar")
    fun updateSeminar(@RequestBody request: SeminarRequest, @RequestHeader("Authorization") token: String, @UserContext userId: Long): UpdateSeminarInfo {
        return seminarService.updateSeminar(request, userId)
    }

    @GetMapping("seminar/{seminar_id}")
    fun getSeminarById(@PathVariable seminar_id: Long): GetSeminarInfo {
        return seminarService.getSeminarById(seminar_id)
    }


    @GetMapping("seminar")
    fun getSeminarList(@RequestParam @Nullable name: String?, @RequestParam @Nullable order: String?): List<GetSeminarInfo> {
        return seminarService.getSeminarList(name, order)
    }


    @Authenticated
    @PostMapping("seminar/{seminar_id}/user")
    fun joinSeminar(@PathVariable seminar_id: Long, @RequestBody role: Map<String, String>, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetSeminarInfo {
        return seminarService.joinSeminar(seminar_id, role, userId)
    }

    @Authenticated
    @DeleteMapping("seminar/{seminar_id}/user")
    fun dropSeminar(@PathVariable seminar_id: Long, @RequestHeader("Authorization") token: String, @UserContext userId: Long): GetSeminarInfo {
        return seminarService.dropSeminar(seminar_id, userId)
    }

   
}