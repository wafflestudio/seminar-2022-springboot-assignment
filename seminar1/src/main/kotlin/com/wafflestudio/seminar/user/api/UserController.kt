package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.survey.api.Seminar400
import com.wafflestudio.seminar.survey.api.Seminar403
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.SeminarService
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController // @controller + responseBody
@RequestMapping("/api/v1")
class UserController (private val userService : UserService, private val seminarService: SeminarService){
    @PostMapping("/user")
    fun createUser(@RequestBody userDto : CreateUserRequest):String {
        userService.join(userDto)
        return "유저 생성 성공"
    }
    
    @PostMapping("/login")
    fun login(@RequestBody loginDto : LoginUserRequest) : String {
        return "유저 ID : " + userService.login(loginDto.email, loginDto.password).toString() + " 로그인 성공"
    } 
    
    @GetMapping("/user/me")
    fun getUser(@RequestHeader(value = "X-User-ID", required = false) user_id : String?):User {
        if(user_id == null) throw Seminar403("X-User_ID 헤더가 존재하지 않습니다.")
        return userService.findMe(user_id.toLong())
    } 
    
    @PostMapping("/survey")
    fun joinSurvey(@RequestHeader header : HttpHeaders, @RequestBody survey : Map<String,String>) : SurveyResponse{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        
        
        
        val request = CreateSurveyRequest(
            osName = survey["osName"] ?: null,
            springExp = survey["springExp"]?.toInt() ?: null,
            rdbExp = survey["rdbExp"]?.toInt() ?: null,
            programmingExp = survey["programmingExp"]?.toInt() ?: null,
            major = survey["major"]?:null,
            grade = survey["grade"]?:null, 
            timestamp = LocalDateTime.parse(LocalDateTime.now().format(formatter),formatter),
            backendReason = survey["backendReason"]?: null,
            waffleReason = survey["waffleReason"]?:null,
            somethingToSay = survey["somethingToSay"]?:null,
            user_id = header.getFirst("X-User-ID")?.toLong() ?: throw Seminar400("User ID가 파싱되지 않은채로 설문을 시도했습니다.")
        )
        
        val id = seminarService.createSurvey(request)
        
        // Return 값이 필요없는 api라서 그냥 값을 반환안해도 되지만,
        // 깔끔하게 설문조사 참여 결과를 보여주기 위해 생성한 SurveyResponseEntity의 id값을 갖고 다시한번 SurveyResponse를 조회하여 반환함.
        return seminarService.surveyResponse(id)
       
    } 
    
    
    
}