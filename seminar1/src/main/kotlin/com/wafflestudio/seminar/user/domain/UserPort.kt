package com.wafflestudio.seminar.user.domain

import org.springframework.stereotype.Component

@Component
interface UserPort {
    fun getUser(id: Int): User
}