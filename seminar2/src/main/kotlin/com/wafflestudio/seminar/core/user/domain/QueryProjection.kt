package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime

data class QueryProjection (
    val username: String,
    val email: String,
    val joinedAt: LocalDateTime
){
    
}