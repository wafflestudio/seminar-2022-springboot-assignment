package com.wafflestudio.seminar.user.domain

data class User (
    val userId : Long,
    val nickname : String,
    val email : String,
    val password : String
    )