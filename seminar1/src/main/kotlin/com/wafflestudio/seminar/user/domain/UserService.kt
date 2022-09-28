package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.user.api.request.CreateUserRequest


interface UserService {
    fun getUser(id: Int): User
    fun createUser(user: User)
    fun login(email: String, password: String) : String
    fun myInfo(email: String) : User
}