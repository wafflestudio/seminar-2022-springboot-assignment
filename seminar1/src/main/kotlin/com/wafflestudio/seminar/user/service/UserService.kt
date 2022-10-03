package com.wafflestudio.seminar.user.service

import com.wafflestudio.seminar.survey.api.Seminar409
import com.wafflestudio.seminar.survey.api.Seminar401
import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.user.api.request.LoginRequest
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService{
    fun signUp(newUser: UserEntity):UserEntity
    fun getUserById(id:Long): UserEntity?
    fun signIn(request: LoginRequest):Long?
}


@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
): UserService{
    override fun signUp(newUser: UserEntity): UserEntity {
        val users=userRepository.findAll();
        for (user in users) {
            if(user.email==newUser.email)throw Seminar409("이메일이 중복되었습니다");
        }
        newUser.password=passwordEncoder.encode(newUser.password);
        userRepository.save(newUser);
        return newUser;
    }
    

    override fun getUserById(id: Long): UserEntity? {
        val users = userRepository.findAll();
        for(user in users){
            if(user.id==id)return user
        }
        throw Seminar404("유저를 찾을 수 없습니다");
    }

    override fun signIn(request: LoginRequest): Long? {
        val users=userRepository.findAll();
        for(user in users){
            if(user.email==request.email){
                if(passwordEncoder.matches(request.password,user.password))return user.id;
                throw Seminar401("비밀번호가 맞지 않습니다");
            }
        }
        throw Seminar404("유저를 찾을 수 없습니다");
    }
    
    
    
}
