package com.wafflestudio.seminar.user.database

import org.apache.catalina.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository:JpaRepository<UserEntity, Long> {
    fun signUpUser(newUser:UserEntity):UserEntity?
    
    fun getUserById(id:Long):UserEntity?
}