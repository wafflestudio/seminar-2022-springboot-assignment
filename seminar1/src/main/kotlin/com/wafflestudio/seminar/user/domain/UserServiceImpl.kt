package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.user.api.request.CreateUserRequest
import com.wafflestudio.seminar.user.api.request.UserException
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import javax.security.auth.message.config.AuthConfig


@Service
class UserServiceImpl(
    private val userPort: UserPort,
    private val userRepository: UserRepository,
) : UserService {
    override  fun getUser(id: Int): User {
        return userPort.getUser(id)
    }
    override fun createUser(user: User){
        val newUser : UserEntity
        if(userRepository.findByEmail(user.email) != null)

            newUser = UserEntity(user.userID, user.email, user.password)
        else throw UserException.Seminar409("중복된 이메일입니다")
        
        userRepository.save(newUser)
    }
    
    override fun login(email: String, password: String): String{
        val newUser : User? = userRepository.findByEmail(email)?.toUser()
        if(newUser == null) throw UserException.Seminar404("존재하지 않는 이메일입니다.")
        else{
            if(newUser.password.equals(password))
                return newUser.userID
            else throw UserException.Seminar401("비밀번호가 틀렸습니다.")
        }
            
    }

    override fun myInfo(email: String): User {
        val newUser : User? = userRepository.findByEmail(email)?.toUser()
        if(newUser == null) throw UserException.Seminar404("존재하지 않는 이메일입니다.")
        else{
                return newUser
        }
    }
}