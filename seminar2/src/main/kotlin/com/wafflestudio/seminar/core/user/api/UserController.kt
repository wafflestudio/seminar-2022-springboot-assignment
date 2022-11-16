package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.UserRequest
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
) {
    @Authenticated
    @GetMapping("/{user_id}/")
    fun getUser(
        @PathVariable("user_id") userId: Long
    ) = userService.getUser(userId)
    
    @Authenticated
    @PutMapping("/me/")
    fun editUser(
        @UserContext userId: Long,
        @Valid @RequestBody userRequest: UserRequest,
    ) = userService.editUser(userId, userRequest)
    
    @Authenticated
    @PostMapping("/participant/")
    fun registerToParticipate(
        @UserContext userId: Long,
        @RequestBody participantRequest: ParticipantRequest,
    ) = userService.registerToParticipate(userId, participantRequest)
}