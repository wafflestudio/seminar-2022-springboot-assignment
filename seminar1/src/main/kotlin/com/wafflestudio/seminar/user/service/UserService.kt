package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.domain.UserInfo

interface UserService {
    fun createUser(nickname: String, email: String, password: String)
    fun login(email: String, password: String)
    fun myInfo(id: Long): UserInfo
}