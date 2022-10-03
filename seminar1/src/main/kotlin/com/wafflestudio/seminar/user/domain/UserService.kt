package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import org.springframework.beans.factory.annotation.Autowired

interface UserService {
    fun getUser(id: Int): User
    fun createUser(user: User): String
    fun login(email: String, password: String) : String
    fun myInfo(email: String) : String
    
    fun survey(userID: String, spring_exp: Int? , rdb_exp: Int? , programming_exp: Int? , os: String? ) : String
}