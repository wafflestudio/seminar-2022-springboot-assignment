package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.request.CreateUserRequest

interface UserService {
    fun createUser(userRequest: CreateUserRequest)
}