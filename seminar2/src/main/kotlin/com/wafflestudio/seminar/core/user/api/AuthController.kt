package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.*
import com.wafflestudio.seminar.core.user.api.response.SeminarResponse
import com.wafflestudio.seminar.core.user.domain.Seminar
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.service.AuthTokenService
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val userService: UserService,
    private val authTokenService: AuthTokenService
) {

    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequest): String {
        userService.signUp(signUpRequest)
        return authTokenService.generateTokenByEmail(signUpRequest.email!!).accessToken
    }

    @PostMapping("/signin")
    fun logIn(@RequestBody loginRequest: LoginRequest): String {
        userService.login(loginRequest.email, loginRequest.password)
        return authTokenService.generateTokenByEmail(loginRequest.email).accessToken
    }

    @Authenticated
    @GetMapping("/me")
    fun getMe(@UserContext userId: Long): User {
        return userService.getProfile(userId)
    }

    @Authenticated
    @GetMapping("/user/{userId}")
    fun getUser(@PathVariable("userId") userId: Long): User {
        return userService.getProfile(userId)
    }

    @Authenticated
    @PutMapping("/user/me")
    fun editMe(@UserContext userId: Long, @Valid @RequestBody editProfileRequest: EditProfileRequest) {
        return userService.editProfile(userId, editProfileRequest)
    }

    @Authenticated
    @PostMapping("/user/participant")
    fun registerParticipant(@UserContext userId: Long, @RequestBody participantRequest: ParticipantRequest) {
        return userService.registerParticipantProfile(userId, participantRequest)
    }

    @Authenticated
    @PostMapping("/seminar")
    fun createSeminar(
        @UserContext userId: Long,
        @Valid @RequestBody createSeminarRequest: CreateSeminarRequest
    ): Seminar {
        return userService.createSeminar(userId, createSeminarRequest)
    }

    @Authenticated
    @PutMapping("/seminar")
    fun editSeminar(@UserContext userId: Long, @Valid @RequestBody editSeminarRequest: EditSeminarRequest): Seminar {
        return userService.editSeminar(userId, editSeminarRequest)
    }

    @Authenticated
    @GetMapping("/seminar/{seminarId}")
    fun getSeminar(@PathVariable("seminarId") seminarId: Long): Seminar {
        return userService.getSeminar(seminarId)
    }

    @Authenticated
    @GetMapping("/seminar")
    fun getSeminars(
        @RequestParam("name") name: String?,
        @RequestParam("order") order: String?
    ): List<SeminarResponse> {
        var nameQuery = ""
        var orderQuery = ""
        if (name != null) {
            nameQuery = name
        }
        if (order != null) {
            orderQuery = order
        }
        return userService.getSeminars(nameQuery, orderQuery)
    }

    @Authenticated
    @PostMapping("/seminar/{seminarId}/user")
    fun joinSeminar(@UserContext userId: Long, @RequestBody joinSeminarRequest: JoinSeminarRequest): Seminar {
        return userService.joinSeminar(userId, joinSeminarRequest.seminarId, joinSeminarRequest.role)
    }

    @Authenticated
    @DeleteMapping("/seminar/{seminarId}/user")
    fun dropSeminar(@UserContext userId: Long, seminarId: Long): Seminar {
        return userService.dropSeminar(userId, seminarId)
    }


}