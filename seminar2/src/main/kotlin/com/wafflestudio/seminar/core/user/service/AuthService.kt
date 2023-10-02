package com.wafflestudio.seminar.core.user.service


import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.user.api.request.EditProfileRequest
import com.wafflestudio.seminar.core.user.api.request.LoginRequest
import com.wafflestudio.seminar.core.user.api.request.Role
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.api.response.ProfileResponse
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.database.profile.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.profile.ParticipantProfileEntity
import org.apache.catalina.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import java.lang.Exception
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@Service
class AuthService(
    val passwordEncoder: PasswordEncoder,
    val userRepository: UserRepository,
    val authTokenService: AuthTokenService
) {
    fun signUp(@RequestBody signUpRequest: SignUpRequest): String{
        // email Exception 구현
        if(userRepository.findByEmail(signUpRequest.email)!=null){
            throw DuplicatedEmailException("이미 존재하는 이메일입니다")
        }
        
        val user = userRepository.save(signUpRequest.toSignUp(passwordEncoder))
        return authTokenService.generateTokenByUsername(user.email).accessToken
    }
    
    
    fun login(@RequestBody loginRequest: LoginRequest): String{
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw WrongEmailException("잘못된 아이디 정보입니다")
        
        if(!checkPassword(user, loginRequest.password))
            throw WrongPasswordException("틀린 비밀번호 입니다")
        user.lastLogin = LocalDateTime.now()
        return authTokenService.generateTokenByUsername(user.email).accessToken
    }
    
    fun getProfile(user_id: Long, request: HttpServletRequest): ProfileResponse{
        val user: UserEntity = userRepository.findById(user_id).get()
        return ProfileResponse.toProfileResponse(user)
    }
    
    fun editProfile(request: HttpServletRequest, @RequestBody editProfileRequest: EditProfileRequest) : ProfileResponse{
        val email : String = request.getAttribute("email").toString()
        val user : UserEntity? = userRepository.findByEmail(email)
        
        user ?: throw WrongEmailException("잘못된 토큰 정보입니다.")
        
        val university = editProfileRequest.university
        val company = editProfileRequest.company
        val careerYear = editProfileRequest.CareerYear
        val username = editProfileRequest.username
        
        if(university != null && user.participantProfileEntity != null)
            user.participantProfileEntity!!.university = university
        if(company != null && user.instructorProfileEntity != null)
            user.instructorProfileEntity!!.company = company
        if(careerYear != null && user.instructorProfileEntity != null){
            if(careerYear < 1){
                throw InValidYearInputException("자연수를 입력해주세요")
            }
            
            user.instructorProfileEntity!!.careerYear = careerYear
        }
        if(username != null)
            user.userName = username
        
        userRepository.save(user)
        return ProfileResponse.toProfileResponse(user)
    }
    private fun checkPassword(user: UserEntity, password: String): Boolean{
        return passwordEncoder.matches(password, user.password)
    }
    
    
}