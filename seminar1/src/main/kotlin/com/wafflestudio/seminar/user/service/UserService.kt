package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.user.api.request.AuthUserRequest
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User

interface UserService {
    fun create(user: CreateUserRequest) : User
    fun login(userinfo: LoginUserRequest) :User
    fun inquiry(id: Long?) : UserEntity?
    fun surveyCreate(id:Long, request: CreateSurveyRequest): Long?
}