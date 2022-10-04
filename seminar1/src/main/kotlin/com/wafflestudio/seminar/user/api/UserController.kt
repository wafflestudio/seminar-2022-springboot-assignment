package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.domain.UserInfoResponse
import com.wafflestudio.seminar.user.api.request.UserLoginRequest
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class UserController(
        private val userService: UserService
) {
    @PostMapping("/api/v1/user")
    fun createUser(
        @RequestBody @Valid userRequest: CreateUserRequest,
        bindingResult: BindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw CreateUserRequestBodyException(
                    bindingResult.fieldErrors
            )
        }
        userService.createUser(userRequest)
    }
    
    @PostMapping("/api/v1/login")
    fun login(
        @RequestBody @Valid userLogin: UserLoginRequest,
        bindingResult: BindingResult
    ): Long {
        if (bindingResult.hasErrors()) {
            throw CreateUserRequestBodyException(
                    bindingResult.fieldErrors
            )
        }
        return userService.login(userLogin)
    }
    
    @GetMapping("/api/v1/user/me")
    fun getUserInfo(
            @RequestHeader(name="X-User-ID") id: Long?,
    ): UserInfoResponse {
        return userService.getUserInfo(id ?: throw GetUserUnauthorizedException())
    }
    
}