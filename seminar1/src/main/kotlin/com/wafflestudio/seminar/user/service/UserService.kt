package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.UserLoginDTO

interface UserService {
    fun createUser(userRequest: CreateUserRequest)
    fun login(userLogin: UserLoginDTO): String
}