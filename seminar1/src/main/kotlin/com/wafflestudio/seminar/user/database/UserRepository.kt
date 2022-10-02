package com.wafflestudio.seminar.user.database

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository:JpaRepository<UserEntity,String>{
    fun findByEmail(email:String):List<UserEntity>
    fun findById(id: Long): List<UserEntity>
}