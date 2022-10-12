package com.wafflestudio.seminar.core.user.database

import org.springframework.data.jpa.repository.JpaRepository

interface AuthRepository :JpaRepository<UserEntity, Long>{
    fun save(userEntity: UserEntity) : UserEntity
    fun findByEmail(email: String): UserEntity
}