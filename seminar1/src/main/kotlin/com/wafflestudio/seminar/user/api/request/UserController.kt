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
@RequestMapping("/api/v1")
class UserController(
    private val service: UserService,
    private val userRepository: UserRepository,
    private val authConfig: AuthConfig
) {
    

    @PostMapping("/user")
    fun newUser(
        @RequestBody user: CreateUserRequest
    ): String{
        val password = authConfig.passwordEncoder().encode(user.password)
        val newuser = User(user.userID,user.email,password)
        val str = service.createUser(newuser)
        return str
    }
    @PostMapping("/login")
    fun login(
        @RequestParam email: String,
        @RequestParam password: String,
    ): String{
        val newUser : User? = userRepository.findByEmail(email)?.toUser()
        if(newUser == null) throw UserException.Seminar404("존재하지 않는 이메일입니다.")
        else{
            if(authConfig.passwordEncoder().matches(password, newUser.password)) return newUser.userID + "님 환영합니다!"
            else throw UserException.Seminar401("비밀번호가 틀렸습니다.")
        }
    }
    @GetMapping("/user/me")
    fun myinfo(
        @RequestHeader(value = "X-User-ID", required = false) email: String?
    ): String{
        
        if(email == null) throw UserException.Seminar403("유저가 식별되지 않습니다.")
        else {
            
            val str = service.myInfo(email)
            return(str)
        }
        
    }
    
    @PostMapping("user/survey")
    fun mysurvey(
        @RequestHeader(value = "X-User-ID", required = false) userID: String?,
        @RequestParam spring_exp: Int?,
        @RequestParam rdb_exp: Int?,
        @RequestParam programming_exp: Int?,
        @RequestParam os: String?
    ): String{
        if(userID == null) throw UserException.Seminar403("유저가 식별되지 않습니다.")
        else {
            val str = service.survey(userID,spring_exp,rdb_exp, programming_exp, os)
            return str
        }
    }

}