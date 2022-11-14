package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.LogExecutionTime
import com.wafflestudio.seminar.common.LoginUser
import com.wafflestudio.seminar.common.SeminarRequestBodyException
import com.wafflestudio.seminar.core.profile.dto.ParticipantProfileRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.dto.ModifyUserRequest
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserController(
    private val userService: UserService
) {
    private val log = org.slf4j.LoggerFactory.getLogger(javaClass)

    @LogExecutionTime
    @GetMapping("/api/v1/user/{userId}")
    fun getUserInformation(
        @PathVariable userId: Long
    ) = userService.constructUserInformationById(userId)

    @LogExecutionTime
    @PutMapping("/api/v1/user/me")
    fun putUserInformation(
            @Valid @RequestBody modifyUserRequest: ModifyUserRequest,
            bindingResult: BindingResult,
            @LoginUser meUser: UserEntity?,
    ) = if (bindingResult.hasErrors()) {
        throw SeminarRequestBodyException(bindingResult.fieldErrors)
    } else {
        meUser?.let {
            userService.modifyUserInformation(modifyUserRequest, meUser)
            ResponseEntity<String>("Modified", HttpStatus.OK)
        }
            ?: ResponseEntity<String>("Failed to get user information.", HttpStatus.UNAUTHORIZED)
    }

    @LogExecutionTime
    @PostMapping("/api/v1/user/participant")
    fun postParticipantPost(
        @RequestBody participantProfileRequest: ParticipantProfileRequest,
        @LoginUser meUser: UserEntity?,
    ): Any = meUser?.let {
        userService.addToParticipantAndReturnUserInfo(participantProfileRequest, meUser)
    } ?: ResponseEntity<String>("Failed to get user information.", HttpStatus.UNAUTHORIZED)
}