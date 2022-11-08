package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.core.user.api.request.EditProfileRequest
import com.wafflestudio.seminar.core.user.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.core.user.service.UserService
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.`as`
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@SpringBootTest
internal class UserServiceTest @Autowired constructor(
        private val userService: UserService,
        private val userTestHelper: UserTestHelper,
        private val userRepository: UserRepository,
) {


    @Test
    fun `테스트 1 - 유저를 만들 수 있다`() {
        // given
        val request1 = SignUpRequest("w1@affle.com", "", "", "PARTICIPANT", "waffle", true)
        val request2 = SignUpRequest("w2@affle.com", "", "", "PARTICIPANT")
        val request3 = SignUpRequest("w3@affle.com", "", "", "INSTRUCTOR", null, null, "waffle", 0)
        val request4 = SignUpRequest("w4@affle.com", "", "", "INSTRUCTOR")

        // when
        userService.signUp(request1)
        userService.signUp(request2)
        userService.signUp(request3)
        userService.signUp(request4)

        // then

        assertThat(userRepository.findAll()).hasSize(4)
    }

    @Test
    fun `테스트 2 - 유저를 만들고 로그인 할 수 있다`() {
        // given
        userTestHelper.createUser("w1@affle.com", password = "1234")
        val request = SignInRequest("w1@affle.com", "1234")

        // when

        val result = userService.signIn(request)

        // then
        assertThat(result.accessToken).isNotEmpty
        assertThat(userRepository.findAll()).hasSize(1)
    }
    
    @Test
    @Transactional  // entity가 영속성의 컨텍스트에 있지 않기 때문에, transaction을 걸어 테스트동안 계속 유지하도록 해둠
    fun `테스트 3 - 유저를 인증할 수 있다`(){
        // given
        val request = userTestHelper.createInstructorUser("w1@affle.com", password = "1234")
        val id = request.id
        // when
        val result = userService.getProfile(id)
        
        // then
        assertThat(result.email).isEqualTo("w1@affle.com")
    }
    
    @Test
    @Transactional
    fun `테스트 4 - 프로필을 수정할 수 있다 + 진행자와 수강생 모두 작동`() {
        // given
        val request1 = userTestHelper.createInstructorUser("w1@affle.com", password = "1234", company = "waffle", year1 = 0)
        val request2 = userTestHelper.createParticipantUser("w2@affle.com", password = "1234", university = "waffle")
        val editRequest1 = EditProfileRequest("gyu", company = "waffle1", year = 1)
        val editRequest2 = EditProfileRequest("seonggyu", university = "waffle1")   
        
        // when
        val result1 = userService.editProfile(request1.id, editRequest1)
        val result2 = userService.editProfile(request2.id, editRequest2)
        
        // then
        assertThat(userRepository.findAll()).hasSize(2)
        assertThat(request1.instructorProfile?.company).isEqualTo("waffle1")
        assertThat(request2.participantProfile?.university).isEqualTo("waffle1")
    }
    
    
    @Test
    @Transactional
    fun `테스트 5 - 진행자도 수강생으로 등록할 수 있다`() {
        
        // given
        val request = userTestHelper.createInstructorUser("w1@affle.com", password = "1234", company = "waffle", year1 = 0)
        val participant = RegisterParticipantRequest("waffle")
        
        // when
        val result = userService.registerParticipant(request.id,participant)
    
        // then
        assertThat(userRepository.findAll()).hasSize(1)
        assertThat(request.participantProfile?.university).isEqualTo("waffle")
    }
}