package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.config.AuthConfig
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.config.web.servlet.headers.HeadersSecurityMarker
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.header.Header
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.transaction.Transactional

@RestController
@RequestMapping("/api/v1")
class UserController (
    private val service: UserService
) {

    @RequestMapping(
        path = ["/user"],
        method = [RequestMethod.POST]
    )
    fun createUser (
        @RequestBody userRequest: CreateUserRequest,
        @Autowired authConfig: AuthConfig
    ): String? = service.UserRegister(userRequest, authConfig)
    
    
    @RequestMapping(
        path = ["/login"],
        method = [RequestMethod.POST],
        headers = ["X-User-ID"]
    )
    fun loginUser (
        @RequestHeader(name = "X-User-ID") values: String,
        @Autowired authConfig: AuthConfig
    ) :Long? = service.UserLogin(values, authConfig)
    
    
    @RequestMapping(
        path = ["/user/me"],
        method = [RequestMethod.GET],
        headers = ["X-User-ID"]
    )
    fun selfUser (
        @RequestHeader(name = "X-User-ID") values: String?,
        @Autowired authConfig: AuthConfig
    ) :String? = service.UserGetInfo(values, authConfig)

    
    @RequestMapping(
        path = ["/survey"],
        method = [RequestMethod.POST],
        headers = ["X-User-ID"]
    )
    fun surveyUser (
        @RequestBody surveyRequest: CreateSurveyRequest,
        @RequestHeader(name = "X-User-ID") values: String,
        @Autowired authConfig: AuthConfig
    ) = service.UserSurveyResponse(values, surveyRequest, authConfig)
    
}