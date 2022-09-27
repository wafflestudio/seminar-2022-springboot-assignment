package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.user.api.User409
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.domain.UserLogin
import com.wafflestudio.seminar.user.domain.User
import org.springframework.stereotype.Service

interface UserService {
    
    fun save(user: UserEntity) : User
    fun findAll() : List<UserEntity>?
    fun findByEmailAndPassword(user: UserLogin): User
    fun findByEmail(user: UserEntity): UserEntity?
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {
    override fun save(user: UserEntity): User {

        if (findByEmail(user) == null){
            val entity = userRepository.save(user)
            return User(entity)
        }
       
        else {
            throw User409("이메일이 중복되었습니다")
        }
       
    }
    
    override fun findAll() : List<UserEntity>? {    
        return userRepository.findAll()
    }

    override fun findByEmail(user: UserEntity): UserEntity? {
        val entity = userRepository.findByEmail(user.email)
        return entity
    }
    override fun findByEmailAndPassword(user: UserLogin): User {
        val entity = userRepository.findByEmailAndPassword(user.email, user.password)
        
        return User(entity)
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