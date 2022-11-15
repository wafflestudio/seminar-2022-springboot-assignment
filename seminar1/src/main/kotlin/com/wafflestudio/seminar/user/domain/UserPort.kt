package com.wafflestudio.seminar.user.domain

interface UserPort {
    fun getUser(id: Int): User
    fun register(name: String, email: String, password: String)
    fun login(email: String, password: String): Long
}