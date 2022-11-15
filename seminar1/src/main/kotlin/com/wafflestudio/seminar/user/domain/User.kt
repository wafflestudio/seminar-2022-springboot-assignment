package com.wafflestudio.seminar.user.domain

import com.fasterxml.jackson.annotation.JsonIgnore

data class User(
    val name: String,
    val email: String,
    val id: Long,
    @JsonIgnore
    val password: String
)