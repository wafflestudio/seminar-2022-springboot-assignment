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
    fun login( @RequestBody user: UserLogin ): Long {
        val loginUser = service.findByEmailAndPassword(user)
        return loginUser.id
    }
    
    @GetMapping("/user/me")
    fun checkMe(@RequestHeader("X-User-Id") value: Long?): UserEntity?{
       
        if(value == null) {
            throw Seminar403("접근할 수 없습니다")
        }
        
        return service.checkUser(value)
    }
    
    @PostMapping("survey")
    fun survey(@RequestBody survey: UserSurvey, @RequestHeader("X-User-Id") value: Long?): SurveyResponseEntity{
        if(value == null) {
            throw Seminar403("접근할 수 없습니다")
        } else if(survey.os == "" || survey.springExp == 0 || survey.rdbExp == 0 || survey.programmingExp == 0) {
            throw Seminar400("필수 입력 칸을 입력해야 합니다")
        }
        return service.survey(survey, value)
    }
    
     
}


