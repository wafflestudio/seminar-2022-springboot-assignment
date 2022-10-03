package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.user.api.request.*
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import com.wafflestudio.seminar.user.domain.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional


interface UserService {
    fun register(userdata:CreateUserRequest): User
    fun login(userdata: LoginUserRequest): Long
    fun getById(userid: Long): User
}


@Service
class UserServiceImpl(
    private val UserRepository: UserRepository,
):UserService {
    @Transactional
    override fun register(userdata:CreateUserRequest): User {
        var encoder: PasswordEncoder= BCryptPasswordEncoder()
        val user=UserEntity(userdata.nickname, encoder.encode(userdata.password), userdata.email)
        try {
            UserRepository.save(user)
        }catch (e: Exception) {
            throw Seminar409("email 다시 입력!")
        }
        return User(userdata.nickname, userdata.password, userdata.email)
    }
    
    @Transactional
    override fun login(userdata: LoginUserRequest): Long{
        var encoder: PasswordEncoder= BCryptPasswordEncoder()
        try{
            UserRepository.findByEmail(userdata.email)
        }catch (e: Exception){
            throw Seminar404("없는 user 입니다. ")
        }
        var user: UserEntity=UserRepository.findByEmail(userdata.email)
        if(encoder.matches(userdata.password, user.password)){
            
        }else{
            throw Seminar401("password가 틀렸습니다.")
        }
        return user.id
    }

    @Transactional
    override fun getById(id: Long): User {
        try{
            UserRepository.findByIdOrNull(id)
        }catch(e: Exception){
            throw Seminar404("존재하지 않는 id 입니다.")
        }
        var user: UserEntity? =UserRepository.findByIdOrNull(id)
        if (user != null) {
            return User(user.nickname , user.password, user.email)
        }
        return User("", "", "")
    }
}