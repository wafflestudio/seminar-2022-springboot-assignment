package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.*
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.UserLogin
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface UserService {
    
    fun save(user: UserEntity) : User
    fun findAll() : List<UserEntity>?
    fun findByEmailAndPassword(user: UserLogin): User
    fun findByEmail(email: String): UserEntity?
    
    fun checkMe(value: Long): UserEntity?
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun save(user: UserEntity): User {

        if (findByEmail(user.email) == null){
            val entity = userRepository.save(user)
            return User(entity)
        }
       
        else {
            throw Seminar409("이메일이 중복되었습니다")
        }
       
    }
    
    override fun findAll() : List<UserEntity>? {    
        return userRepository.findAll()
    }

    override fun findByEmail(email: String): UserEntity? {
        return userRepository.findByEmail(email)
    }
    
    override fun findByEmailAndPassword(user: UserLogin): User {
        
        
        if(findByEmail(user.email)?.password != user.password){
            
            throw Seminar401("비밀번호가 틀렸습니다")
        } else {
            val entity = userRepository.findByEmailAndPassword(user.email, user.password)
            return User(entity)
        }
        
        
    }

    
    
    override fun checkMe(value: Long): UserEntity? {

        
        return userRepository.findByIdOrNull(value) ?: throw Seminar400("존재하지 않는 유저입니다")
    }
    

    private fun User(entity: UserEntity) = entity.run {
        User(
            id = id,
            nickname = nickname,
            email = email, 
            password = password
        )
    }
}