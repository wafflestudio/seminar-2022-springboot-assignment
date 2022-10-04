package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.domain.UserInfoResponse
import com.wafflestudio.seminar.user.api.request.UserLoginRequest

interface UserService {
    fun createUser(userRequest: CreateUserRequest)
    fun login(userLogin: UserLoginRequest): Long
    fun getUserInfo(id: Long): UserInfoResponse
}