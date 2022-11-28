package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.UserDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
internal class UserServiceImplTest @Autowired constructor(
    private val userService: UserService,
) {

    @Test
    @Transactional
    fun signUpSucceed() {
        //given
        val signUpRequest: UserDto.SignUpRequest = createSignUpRequestParticipant()
        
        //when
        val result: AuthToken = userService.signUp(signUpRequest)
        
        //then
        Assertions.assertThat(result.accessToken).isNotNull
        
    }

    fun createSignUpRequestParticipant(
        email: String = "emailDefault",
        username: String = "usernameDefault",
        password: String = "passwordDefault",
        university: String? = "",
        isRegistered: Boolean? = true,
    ): UserDto.SignUpRequest {
        return UserDto.SignUpRequest(
            email = email,
            username = username,
            password = password,
            role = UserDto.Role.PARTICIPANT,
            university = university,
            isRegistered = isRegistered,
            company = null,
            year = null,
        )
    }
}