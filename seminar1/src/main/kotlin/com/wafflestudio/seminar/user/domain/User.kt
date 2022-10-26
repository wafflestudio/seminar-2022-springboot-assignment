package com.wafflestudio.seminar.user.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

data class User(
    val id: Long,
    var name: String?,
    var email: String?,
    @JsonIgnore
    // decoder 존재 x -> 생각해보면 요즘 비번 찾기 해도 이전에 설정한 비번을 보여주기보단 새로 설정하라고 함
    var password: String,
)