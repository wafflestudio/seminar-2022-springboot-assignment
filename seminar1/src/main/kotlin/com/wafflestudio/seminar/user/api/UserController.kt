package com.wafflestudio.seminar.user.api


import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.DoLoginRequest
import com.wafflestudio.seminar.user.domain.User
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1")
class UserController(
    private val service: UserService
) {
    // @RequestBody 말고 @Valid는 약간 다름.
    @PostMapping("/user")
    fun createUser(@RequestBody req: CreateUserRequest) = service.saveUser(req)

    @PostMapping("/login")
    fun doLogin(@RequestBody req: DoLoginRequest): Long = service.doLogin(req)

    @GetMapping("/user/me")
    fun getThisUser(
        @RequestHeader(value = "X-User-ID", required = true) userId: Long
    ): User = service.getUser(userId)
    


    @PostMapping("/survey")
    fun createSurvey(
        @RequestHeader(value = "X-User-ID", required = true) userId: Long,
        @RequestBody createSurveyRequest: CreateSurveyRequest
    ) = service.createSurveyResponse(createSurveyRequest, userId)
    
    
}