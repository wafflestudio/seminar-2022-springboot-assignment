package com.wafflestudio.seminar.user.domain

data class User(
    val name :String,
    val email: String,
    val pw: String,
    val id: Long
)