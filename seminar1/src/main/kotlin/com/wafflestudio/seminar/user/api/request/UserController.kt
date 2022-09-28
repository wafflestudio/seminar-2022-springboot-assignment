package com.wafflestudio.seminar.user.api.request

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.domain.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val service: UserService,
    private val userRepository: UserRepository,
    private val authConfig: AuthConfig
) {
    @RequestMapping("/api/v1")

    @PostMapping("/user")
    fun newUser(
        @RequestBody user: CreateUserRequest
    ){
        val password = authConfig.passwordEncoder().encode(user.password)
        val newuser = User(user.userID,user.email,password)
        service.createUser(newuser)



    }
    @PostMapping("/login")
    fun login(
        @RequestParam email: String,
        @RequestParam password: String,
    ){
        val password = authConfig.passwordEncoder().encode(password)
        service.login(email, password)
    }
    @GetMapping("/user/me")
    fun myinfo(
        @RequestHeader(value = "X-User-ID", required = false) email: String
    ){
        val user: User
        if(email == null) throw UserException.Seminar403("유저가 식별되지 않습니다.")
        else user = service.myInfo(email)
        println(user.toString())
    }

}