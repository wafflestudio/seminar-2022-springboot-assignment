package com.wafflestudio.seminar.core.user.dto

import com.wafflestudio.seminar.core.profile.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.profile.database.InstructorProfileRepository
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.profile.database.ParticipantProfileRepository
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.UserException400
import org.springframework.lang.Nullable
import org.springframework.security.crypto.password.PasswordEncoder
import javax.validation.constraints.*

data class SignUpRequest(
        @field: NotBlank(message = "Email should not be empty.")
        @field: Email(message = "Not an email format.")
        val email: String? = null,
        
        @field: NotBlank(message = "Username should not be empty.")
        val username: String? = null,
        
        @field: NotEmpty(message = "Password should not be empty.")
        val password: String? = null,
        
        @field: Pattern(regexp = "participant|instructor", message = "Wrong role input given")
        val role: String? = null,
        
        // participant
        val university: String = "",
        val isRegistered: Boolean = true,
        
        // instructor
        val company: String = "",
        
        @field: Positive
        @field: Nullable
        val year: Int? = null, 
) {
        fun saveAndGetUser(encoder: PasswordEncoder,
                           userRepository: UserRepository,
                           participantProfileRepository: ParticipantProfileRepository,
                           instructorProfileRepository: InstructorProfileRepository
        ): UserEntity {
                return when (role) {
                        "participant" -> {
                                val userEntity = UserEntity(
                                        username!!,
                                        email!!,
                                        encoder.encode(password!!),
                                )
                                val participantProfileEntity = ParticipantProfileEntity(
                                        university,
                                        isRegistered,
                                        userEntity
                                )
                                userEntity.participantProfile = participantProfileEntity
                                participantProfileRepository.save(participantProfileEntity)
                                userRepository.save(userEntity)
                        }
                        "instructor" -> {
                                val userEntity = UserEntity(
                                        username!!,
                                        email!!,
                                        encoder.encode(password!!),
                                )
                                val instructorProfileEntity = InstructorProfileEntity(
                                        company, 
                                        year,
                                        userEntity
                                )
                                userEntity.instructorProfile = instructorProfileEntity
                                instructorProfileRepository.save(instructorProfileEntity)
                                userRepository.save(userEntity)
                        }
                        else -> { // Cannot be executed
                                throw UserException400("Wrong role input given")
                        }
                }
        }
}