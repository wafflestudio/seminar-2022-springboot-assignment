package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar403
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.UserLogin
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.domain.UserSurvey
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*
import java.net.http.HttpHeaders


@RestController
@RequestMapping("/api/v1")
class UserController(private val service: UserService) {
    
    @GetMapping("")
    fun index(){
        
    }

    @GetMapping("/find")
    fun find(): List<UserEntity>? {
        return service.findAll()
    }
    
    @PostMapping("/user")
    fun save( @RequestBody user: UserEntity
    ):User {
        return service.save(user)
    }
    
    @PostMapping("/login")
    fun login( @RequestBody user: UserLogin ): User {
        return service.findByEmailAndPassword(user)
        
    }
    
    @GetMapping("/user/me")
    fun checkMe(@RequestHeader("X-User-Id") value: Long?): UserEntity?{
       
        if(value == null) {
            throw Seminar403("접근할 수 없습니다")
        }
        
        return service.checkUser(value)
    }
    
    @PostMapping("survey")
    fun survey(@RequestBody survey: UserSurvey, @RequestHeader("X-User-Id") value: Long): SurveyResponseEntity{
        
        return service.survey(survey, value)
    }
    
     
}


