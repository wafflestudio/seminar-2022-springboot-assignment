package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.user.api.request.ParticipateRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.Participant
import com.wafflestudio.seminar.core.user.domain.Profile
import com.wafflestudio.seminar.core.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService{
    fun join(user: SignUpRequest)
    fun login(email : String, password : String) : Long
    fun findMe(user_id : Long): User
    fun findAll():List<User>
    fun loadProfile(user_id : Long): Profile
    fun participate(user_id : Long, request : ParticipateRequest)
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService{
    
    override fun join(user: SignUpRequest) {
        val temp = userRepository.findByEmail(user.email)
        if(( user.role in Role.values().map{toString()})){
            if( temp == null){
                userRepository.save(UserEntity(user.username, user.email, passwordEncoder.encode(user.password),
                    Role.valueOf(user.role)))
            }
            else{
                throw Seminar409("이미 존재하는 email 입니다.")
            }
        }
        else{
            throw Seminar400("Role 을 잘못 설정했습니다.")
        }
    }

    override fun login(email: String, password: String): Long {
        val userEntity = userRepository.findByEmail(email)
        if (userEntity == null){
            throw Seminar404("존재하지 않는 email 입니다.")
        }
        else{
            if(passwordEncoder.matches(password, userEntity.encodedPassword)){
                return userEntity.id
            } else{
                throw Seminar401("비밀번호가 틀립니다.")
            }
        }
    }

    override fun findMe(user_id: Long): User {
        return userRepository.findByIdOrNull(user_id)?.toUser() ?: throw Seminar404("존재하지 않는 유저입니다.")
    }
    override fun findAll():List<User>{
        return userRepository.findAll().map{it.toUser()}
    }

    override fun loadProfile(user_id: Long): Profile {
        return userRepository.findByIdOrNull(user_id)?.toProfile() ?: throw Seminar404("존재하지 않는 유저입니다.") 
    }

    override fun participate(user_id: Long, request: ParticipateRequest) {
        val profile = loadProfile(user_id)
        
        val newParticipant = 
            Participant(
            id = request.id,
            university = request.university,
            isRegistered = request.isRegistered,
        )
        if(profile.participant != null){
            throw Seminar409("이미 참여자인 사람입니다.")
        }
        profile.participant = newParticipant
        throw Seminar200(profile.toString())
    }
    
}