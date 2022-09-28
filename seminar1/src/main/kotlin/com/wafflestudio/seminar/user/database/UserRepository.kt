package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import org.apache.catalina.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String) : UserEntity?
    
}