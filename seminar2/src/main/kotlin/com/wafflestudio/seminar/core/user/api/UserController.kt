package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.UpdateUserRequest
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    @Authenticated
    @GetMapping("/api/v1/me")
    fun getMe(
        @UserContext userId: Long,
    ): User {
        return userService.getUser(userId)
    }

    @Authenticated
    @PutMapping("/api/v1/me")
    fun updateMe(
        @UserContext userId: Long,
        @RequestBody request: UpdateUserRequest
    ): User {
        return userService.update(userId, request)
    }

    @Authenticated
    @GetMapping("/api/v1/{userId}")
    fun getUser(
        @UserContext currentUserId: Long,
        @PathVariable userId: Long,
    ): User {
        return userService.getUser(userId)
    }

    @Authenticated
    @PostMapping("/api/v1/user/participant")
    fun registerAsParticipant(
        @UserContext userId: Long,
        @RequestBody request: RegisterParticipantRequest,
    ): User {
        return userService.registerParticipant(userId, request)
    }

}