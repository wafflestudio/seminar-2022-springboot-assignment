package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.ErrorCode
import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.EditProfileRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRoleRequest
import com.wafflestudio.seminar.core.user.domain.UserDTO
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.transaction.Transactional
import javax.validation.Valid

@RestController
class UserController (
    private val userService: UserService 
){
    
    @Authenticated
    @GetMapping("/api/v1/user/{user_id}") 
    fun getUserProfile(
        @RequestHeader("Authorization") accessToken: String,
        @UserContext userId: Long,
        @PathVariable("user_id") targetUserId: Long
    ) : UserDTO = userService.getUserProfile(userId, targetUserId)
    
    
    @Authenticated
    @Transactional
    @PutMapping("/api/v1/user/me")
    fun editProfile(
        @RequestHeader("Authorization") accessToken: String,
        @UserContext userId: Long,
        @Valid @RequestBody request: EditProfileRequest
    ) : UserDTO {
        if(request.email != null) throw SeminarException(ErrorCode.EDIT_EMAIL_FORBIDDEN)
        return userService.editProfile(userId, request)
    }
    
    
    
    @Authenticated
    @Transactional
    @PostMapping("/api/v1/user/participant")
    // 아직 수강 권한이 없는 유저 대상으로 수강 권한 부여
    fun addParticipantRole(
        @UserContext userId: Long,
        @RequestBody request: ParticipantRoleRequest
    ) : UserDTO = userService.addParticipantRole(userId, request)
}