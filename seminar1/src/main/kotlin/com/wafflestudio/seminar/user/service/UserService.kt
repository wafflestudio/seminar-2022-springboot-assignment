package com.wafflestudio.seminar.survey.service

interface UserService {
    fun signIn(email: String, password: String): String
    fun signUp(email: String, password: String)
    fun getUserMe(id: String)
}