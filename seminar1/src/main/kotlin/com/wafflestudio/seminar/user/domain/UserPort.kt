package com.wafflestudio.seminar.user.domain

interface UserPort {
    fun getUser(id:Long):User
}