package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.UserResponse
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder : PasswordEncoder,
    private val osRepository: OsRepository,
)
{
    
    @Transactional /**make it nullable and then implement at the controller*/
    fun saveUser(request: CreateUserRequest): UserResponse? { 
        val user =userRepository.findByEmail(request.email)
        user?.let{
            val newUser = UserEntity(request.nickname, request.email,this.passwordEncoder.encode(request.password))
            userRepository.save(newUser) // since we extend JPA repository, no need to implement save method myself
            return UserResponse(newUser.id,newUser.nickname,newUser.email)
        }?:run{throw IllegalStateException("이미 존재하는 이메일입니다")}
        
    }
    
    @Transactional
    fun login(request: LoginUserRequest) : UserResponse? {
        val user  = userRepository.findByEmail(request.email)
        user?.let{
            if (!passwordEncoder.matches(request.password ,user.password)) {
                throw IllegalStateException("비밀번호가 틀렸습니다.")
            }
            return UserResponse(user.id,user.nickname,user.email)
        } ?:run{throw IllegalStateException("존재하지 않는 사용자입니다.")}
    }
    
    fun findById(id : Long) : UserEntity?{
        val result=userRepository.findById(id)
        result.ifPresent(throw IllegalStateException("존재하지 않는 사용자입니다."))
        return result.get()
    }
    
    @Transactional
    fun participate(id: Long,request: CreateSurveyRequest) {
        val user=userRepository.findById(id)
        user.ifPresent(throw IllegalStateException("존재하지 않는 사용자입니다."))
        val os=osRepository.findByOsName(request.operatingSystem)
        os?.let{
            user.get().updateSurvey(request,os)
        }?:run{throw IllegalStateException("존재하지 않는 os입니다.")}
    }
}
   
