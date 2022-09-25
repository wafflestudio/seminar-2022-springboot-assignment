package com.wafflestudio.seminar.user.database

import com.wafflestudio.seminar.user.domain.User1
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User1, Long>{
}