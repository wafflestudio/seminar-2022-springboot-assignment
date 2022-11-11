package com.wafflestudio.seminar.core.user

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.user.api.request.CreateInstructorDTO
import com.wafflestudio.seminar.core.user.api.request.CreateParticipantDTO
import com.wafflestudio.seminar.core.user.api.request.SignInRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserServiceTest @Autowired constructor(
    private val userService: UserService,
    private val userTestHelper: UserTestHelper,
    private val userRepository: UserRepository,
) {
    @BeforeEach
    fun cleanUp() {
        userRepository.deleteAll()
    }

    @Test
    fun `회원가입 - instructor 로 회원가입 할 수 있다`() {
        // given
        val instructorRequest = CreateInstructorDTO("company", 10)
        val request = SignUpRequest("email@waffle.com", "", "", "instructor", null, instructorRequest)

        // when
        userService.createUser(request)

        // then
        val user = userRepository.findByEmail(request.email)
        assertThat(user).isNotNull
    }

    @Test()
    fun `회원가입 - 이미 존재하는 이메일이면 400를 반환한다`() {
        // given
        val request = SignUpRequest("email@waffle.com", "", "", "instructor", null, null)

        // when
        userService.createUser(request)
        catchThrowable { userService.createUser(request) }
        val thrown: Throwable = catchThrowable{ userService.createUser(request) }

        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }
    
    @Test
    fun `회원가입 - instructor 의 year 가 음수면 400를 반환한다`() {
        // given
        val instructorRequest = CreateInstructorDTO("company", -1)
        val request = SignUpRequest("email@waffle.com", "", "", "instructor", null, instructorRequest)
        
        // when
        val throwable = catchThrowable { userService.createUser(request) }
        
        // then
        assertThat(throwable).isInstanceOf(Seminar400::class.java)
    }
    
    @Test
    fun `회원가입 - participant 로 회원가입 할 수 있다`() {
        // given
        val participantRequest = CreateParticipantDTO("university", true)
        val request = SignUpRequest("email@waffle.com", "", "", "participant", participantRequest, null)
        
        // when
        userService.createUser(request)
        
        // then
        val user = userRepository.findByEmail(request.email)
        assertThat(user).isNotNull
    }
    
    @Test
    fun `회원가입 - participant 정보가 없으면 400을 반환한다`() {
        // given
        val request = SignUpRequest("email@waffle.com", "", "", "participant", null, null)
        
        // when
        val throwable = catchThrowable { userService.createUser(request) }
        
        // then
        assertThat(throwable).isInstanceOf(Seminar400::class.java)
    }
    
    @Test
    fun `회원가입 - 잘못된 role 이면 400을 반환한다`() {
        // given
        val request = SignUpRequest("email@waffle.com", "", "", "role", null, null)
        
        // when
        val throwable = catchThrowable { userService.createUser(request) }
        
        // then
        assertThat(throwable).isInstanceOf(Seminar400::class.java)
    }
    
    @Test
    fun `로그인 - 유저를 만들고 로그인 할 수 있다`() {
        // given
        userTestHelper.createUser("waffle@studio.com", password = "1234")
        val request = SignInRequest("waffle@studio.com", "1234")

        // when
        val result = userService.loginUser(request)

        // then
        assertThat(result.accessToken).isNotEmpty
        assertThat(userRepository.findAll()).hasSize(1)
    }

}