package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.request.CreateSurveyRequest
import com.wafflestudio.seminar.survey.api.response.CreateSurveyResponse
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.user.api.*
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.LoginUserRequest
import com.wafflestudio.seminar.user.api.response.UserResponse
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.User

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder : PasswordEncoder,
    private val osRepository: OsRepository,
    private val surveyResponseRepository: SurveyResponseRepository
):UserService
{
    @Transactional /**make it nullable and then implement at the controller*/
    override fun saveUser(request: CreateUserRequest): UserResponse? { 
        val user =userRepository.findByEmail(request.email)
        user?.let{
            throw InvalidEmail()
        }?:run{
            val newUser = UserEntity(request.nickname, request.email,this.passwordEncoder.encode(request.password))
            userRepository.save(newUser) // since we extend JPA repository, no need to implement save method myself
            return UserResponse(newUser.id,newUser.nickname,newUser.email)
        }
        
    }
    
    @Transactional
    override fun login(request: LoginUserRequest) : UserResponse? {
        val user  = userRepository.findByEmail(request.email)
        user?.let{
            if (!passwordEncoder.matches(request.password ,user.password)) {
                throw IncorrectPassword()
            }
            return UserResponse(user.id,user.nickname,user.email)
        } ?:run{throw UserNotFound()}
    }

    override fun findById(id : Long) : UserEntity? {
        val result=userRepository.findById(id)
        result.ifPresent(return result.get())
        throw UserNotFound()
    }
    
    @Transactional
    override fun participate(id: Long,request: CreateSurveyRequest) : CreateSurveyResponse? {
        val user=userRepository.findById(id)
        if(user.isPresent){
            val os=osRepository.findByOsName(request.operatingSystem)
            os?.let{
                user.get().updateSurvey(request,os)
                surveyResponseRepository.save(
                    SurveyResponseEntity(
                        os,
                        request.springExp,
                        request.rdbExp,
                        request.programmingExp,
                        request.major,
                        request.grade,
                        request.timestamp,
                        request.backendReason,
                        request.waffleReason,
                        request.somethingToSay,
                        user.get()!!
                    )
                )
                return CreateSurveyResponse(
                    id,
                    request.operatingSystem,
                    request.springExp,
                    request.rdbExp,
                    request.programmingExp,
                    request.major,
                    request.grade,
                    request.timestamp,
                    request.backendReason,
                    request.backendReason,
                    request.waffleReason
                )
            }?:run{ throw Seminar404("OS ${os}을 찾을 수 없어요.")
            }
           
        }
        throw UserNotFound()
    }
    

    override fun userList(): List<User> {
        val userList = userRepository.findAll()
        userList?.let{
            return userList.map(::User)
        }
    }
    private fun User(entity: UserEntity) = entity.run {
        User(
        id=id,
        nickname=nickname,
        email=email,
        password=password
        )
    }
}
   
