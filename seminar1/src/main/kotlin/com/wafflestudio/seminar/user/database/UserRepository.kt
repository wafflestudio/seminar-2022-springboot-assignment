package com.wafflestudio.seminar.user.database

import org.apache.catalina.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository:JpaRepository<UserEntity, Long> 