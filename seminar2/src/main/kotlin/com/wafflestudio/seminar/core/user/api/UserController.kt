package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.UserRequest
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/{user_id}/")
    fun getUser(
        @UserContext @PathVariable("user_id") userId: Long
    ) = userService.getUser(userId)
    
    @PutMapping("/me/")
    fun editUser(
        @UserContext userId: Long,
        @RequestBody userRequest: UserRequest,
    ) = userService.editUser(userId, userRequest)
    
    @PostMapping("/participant/")
    fun registerToParticipate(
        @UserContext userId: Long,
        @RequestBody participantRequest: ParticipantRequest,
    ) = userService.registerToParticipate(userId, participantRequest)
}