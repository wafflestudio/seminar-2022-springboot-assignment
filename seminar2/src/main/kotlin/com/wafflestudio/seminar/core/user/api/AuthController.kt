package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.LogExecutionTime
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.request.UpdateUserRequest
import com.wafflestudio.seminar.core.user.api.response.UserProfile
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.service.AuthException
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.Optional
import javax.validation.Valid


@RestController
class AuthController(
    private val userService: UserService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @LogExecutionTime
    @PostMapping("/api/v1/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest) : ResponseEntity<AuthToken> {
        return ResponseEntity.ok(userService.signUp(request))
    }
    
    @LogExecutionTime
    @PostMapping("/api/v1/signin")
    fun logIn(@Valid @RequestBody request: SignInRequest) : ResponseEntity<AuthToken> {
        return ResponseEntity.ok(userService.signIn(request))
    }
    
    @LogExecutionTime
    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(@UserContext user: Optional<UserEntity>): ResponseEntity<UserProfile> {
        if (user.isEmpty) {
            throw AuthException("유저를 찾을 수 없습니다")
        }
        val userMe = user.get()
        
        return ResponseEntity.ok(userService.createUserProfileFromUser(userMe))
    }
    
    @LogExecutionTime
    @Authenticated
    @PutMapping("/api/v1/me")
    fun updateMe(@UserContext user: Optional<UserEntity>, @Valid @RequestBody request: UpdateUserRequest) : ResponseEntity<UserProfile> {
        if (user.isEmpty) {
            throw AuthException("유저를 찾을 수 없습니다")
        }
        val updatedUserProfile = userService.updateUser(user.get(), request)
        return ResponseEntity.ok(updatedUserProfile)
    }
}