package com.wafflestudio.seminar.user.domain

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseEntity
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.user.api.request.UserException
import com.wafflestudio.seminar.user.database.UserEntity
import com.wafflestudio.seminar.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Service
import javax.security.auth.message.config.AuthConfig
import kotlin.system.exitProcess


@Service
class UserServiceImpl(
    private val userPort: UserPort,
    private val userRepository: UserRepository,
    private val osRepository: OsRepository,
    private val surveyResponseRepository: SurveyResponseRepository

) : UserService {
    override  fun getUser(id: Int): User {
        return userPort.getUser(id)
    }
    override fun createUser(user: User): String{
        val newUser : UserEntity
        
        if(userRepository.findByEmail(user.email) == null)
            
            newUser = UserEntity(user.userID, user.email, user.password)
        
        else throw UserException.Seminar409("중복된 이메일입니다")
        
        userRepository.save(newUser)
        return "계정을 생성하였습니다. email: " + newUser.email + " , ID: " + newUser.userID
    }
    
    override fun login(email: String, password: String): String{
        val newUser : User? = userRepository.findByEmail(email)?.toUser()
        if(newUser == null) throw UserException.Seminar404("존재하지 않는 이메일입니다.")
        else{
            if(newUser.password.equals(password)) {
                val str = newUser.userID + "님 환영합니다!"
                return str
            }
            else throw UserException.Seminar401("비밀번호가 틀렸습니다.")
        }
            
    }

    override fun myInfo(email: String): String {
        val newUser : User? = userRepository.findByEmail(email)?.toUser()
        if(newUser == null) throw UserException.Seminar404("존재하지 않는 이메일입니다.")
        else{
            val str = "email: " + newUser.email + " , ID: " + newUser.userID
                return str
        }
    }

    override fun survey(email: String, spring_exp: Int?, rdb_exp: Int?, programming_exp: Int?, os: String?) : String {
        val newUser :UserEntity? = userRepository.findByEmail(email)
        if(newUser == null) throw UserException.Seminar404("존재하지 않는 이메일입니다.")
        else{
            if(spring_exp == null) throw UserException.Seminar400("spring_exp가 입력되지 않았습니다.")
            if(rdb_exp == null) throw UserException.Seminar400("rdb_exp가 입력되지 않았습니다.")
            if(programming_exp == null) throw UserException.Seminar400("programming_exp가 입력되지 않았습니다.")
            if(os == null) throw UserException.Seminar400("os가 입력되지 않았습니다.")
            else{
                val entity = osRepository.findByOsName(os) ?: throw UserException.Seminar404("OS ${os}을 찾을 수 없어요.")
                val surveyResponseEntity =  SurveyResponseEntity(operatingSystem = entity, springExp = spring_exp, 
                    rdbExp = rdb_exp, programmingExp = programming_exp, user = newUser)
                surveyResponseRepository.save(surveyResponseEntity)
            }
        }
        return "응답이 성공적으로 제출되었습니다!"


    }
}