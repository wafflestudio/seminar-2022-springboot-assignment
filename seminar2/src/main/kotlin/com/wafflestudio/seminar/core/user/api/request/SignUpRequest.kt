package com.wafflestudio.seminar.core.user.api.request

import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.profile.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.profile.ParticipantProfileEntity
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank


data class SignUpRequest(
    @NotBlank
    val email: String,
    @NotBlank
    val username: String,
    @NotBlank
    val password: String,
    @NotBlank
    val role : String,
    
    var university: String?,
    var isRegistered: Boolean?,
    var company: String?,
    var careerYear : Int?
) {
    fun toSignUp(passwordEncoder: PasswordEncoder) : UserEntity{
        if(role == Role.Instructors.label){
            company = company ?: ""
            
            val instructorProfileEntity :InstructorProfileEntity = InstructorProfileEntity(company = company, careerYear = careerYear)
            return UserEntity(
                userName = this.username,
                email = this.email,
                password = passwordEncoder.encode(password),
                instructorProfileEntity = instructorProfileEntity,
                participantProfileEntity = null,
                dateJoined = LocalDateTime.now()
            )
        }else{
            university = university ?: ""
            isRegistered = isRegistered ?: true
            val participantProfileEntity : ParticipantProfileEntity = ParticipantProfileEntity(isRegistered = isRegistered, university = university)
            return UserEntity(
                userName = this.username,
                email = this.email,
                password = passwordEncoder.encode(password),
                instructorProfileEntity = null,
                participantProfileEntity = participantProfileEntity,
                dateJoined = LocalDateTime.now()
            )
        }
        
    }
}


enum class Role(val label: String){
    Participants("participants"), Instructors("instructors");
}