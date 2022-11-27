package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.EditProfileRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val userService: UserService
) {

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
    fun registerParticipant(@UserContext userId: Long, @RequestBody participantRequest: ParticipantRequest): User {
        userService.registerParticipantProfile(userId, participantRequest)
        return userService.getProfile(userId)
    }
}