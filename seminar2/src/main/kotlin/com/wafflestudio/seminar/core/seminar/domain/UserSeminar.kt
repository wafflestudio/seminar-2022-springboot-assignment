package com.wafflestudio.seminar.core.seminar.domain

import java.util.*

data class UserSeminar (
    val id : Long,
    val name : String,
    val joinedAt : Date,
    val isActive : Boolean,
    val droppedAt : Date? = null
    ){
    
}