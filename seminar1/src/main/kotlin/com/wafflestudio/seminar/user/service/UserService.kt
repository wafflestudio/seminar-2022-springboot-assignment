package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.api.response.CreateSurveyResponse
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.UserResponse
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User

interface UserService {
    fun saveUser(request: CreateUserRequest): UserResponse?
    fun login(request: LoginUserRequest) : UserResponse?
    fun findById(id : Long) : UserEntity?
    fun participate(id: Long,request: CreateSurveyRequest) : CreateSurveyResponse?
    fun userList(): List<User>
}