package com.wafflestudio.seminar.core.user.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.LogExecutionTime
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.user.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.user.api.response.UserProfile
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.service.AuthException
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
class UserController(
    private val userService: UserService
) {
    
    @LogExecutionTime
    @Authenticated
    @GetMapping("/api/v1/user/{userId}")
    fun getUser(
        @PathVariable userId: Long
    ) = ResponseEntity.ok(userService.getUser(userId))
    
    @LogExecutionTime
    @Authenticated
    @GetMapping("/api/v1/users")
    fun getUsers(
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam("size", defaultValue = "50") size: Int
    ) : ResponseEntity<Page<UserProfile>> {
        return ResponseEntity.ok(userService.getAllUsers(page, size))
    } 
    
    @LogExecutionTime
    @Authenticated
    @PostMapping("/api/v1/user/participant")
    fun registerParticipantForInstructor(
        @UserContext user: UserEntity, @Valid @RequestBody request: RegisterParticipantRequest
    ) : ResponseEntity<UserProfile> {
        return ResponseEntity.ok(userService.registerParticipantForInstructor(user, request))
    }
    
    @LogExecutionTime
    @Authenticated
    @DeleteMapping("/api/v1/user")
    fun deleteUser(
        @UserContext user: UserEntity
    ) : ResponseEntity<Map<String, Long>>{
        userService.deleteUser(user)
        return ResponseEntity.ok(
            mapOf(
                "deleted_user_id" to user.id
            )
        )
    }
}