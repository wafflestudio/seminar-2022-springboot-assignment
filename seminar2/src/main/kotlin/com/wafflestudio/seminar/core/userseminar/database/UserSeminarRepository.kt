package com.wafflestudio.seminar.core.userseminar.database


import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long> {
    
}