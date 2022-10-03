package com.wafflestudio.seminar.user.service

interface UserService {
    fun createUser(nickname: String, email: String, password: String)
    fun login(email: String, password: String)
}