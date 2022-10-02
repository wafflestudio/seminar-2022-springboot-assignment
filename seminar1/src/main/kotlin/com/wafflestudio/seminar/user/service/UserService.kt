package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.database.UserEntity

interface UserService {
    fun createUser(nickname:String,email:String,password:String)
    fun login(email:String,password:String):Long
    fun getMyInfo(id:Long):UserEntity
}