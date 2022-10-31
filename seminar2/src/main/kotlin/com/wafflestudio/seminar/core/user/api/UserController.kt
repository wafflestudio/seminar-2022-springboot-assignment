package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.UserDto
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserController(
    private val userService: UserService
) {


    @Authenticated
    @GetMapping("/api/v1/user/{user_id}/")
    fun getProfile(
        @PathVariable user_id: Long,
        @RequestHeader(value = "Authorization") authorization: String,
    ): UserDto.UserProfileResponse = userService.getProfile(user_id)

    @Authenticated
    @PutMapping("/api/v1/user/me/")
    fun updateProfile(
        @RequestHeader(value = "Authorization") authorization: String,
        @Valid @RequestBody req: UserDto.UpdateRequest,
        @UserContext userId: Long
    ): UserDto.UserProfileResponse = userService.updateProfile(req, userId)

    @Authenticated
    @PostMapping("/api/v1/user/participant")
    fun registerParticipant(
        @RequestHeader(value = "Authorization") authorization: String,
        @Valid @RequestBody req: UserDto.RegisterParticipantRequest,
        @UserContext userId: Long
    ): UserDto.UserProfileResponse = userService.registerParticipant(req, userId)

}