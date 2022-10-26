package com.wafflestudio.seminar.user.domain

interface UserPort {
    fun getUser(id: Long): User
    fun register(name: String, email: String, pwd: String): String
    fun login(email: String, pwd: String): Long
}