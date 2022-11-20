package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.api.request.EditUserRequest
import com.wafflestudio.seminar.core.user.api.request.ParticipantRequest
import com.wafflestudio.seminar.core.user.api.response.UserResponse
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name="users")
class UserEntity(
    var username: String,
    val email: String,
    var password: String,
    var lastLogin: LocalDateTime,
    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var participantProfile: ParticipantProfile? = null,
    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var instructorProfile: InstructorProfile? = null
) : BaseTimeEntity() {
    fun toUserResponse(userSeminarRepository: UserSeminarRepository) : UserResponse {
        return UserResponse(
            id = id,
            username = username,
            email = email,
            lastLogin = lastLogin,
            dateJoined = createdAt!!,
            participant = participantProfile?.toParticipantProfileResponse(userSeminarRepository.findAllByUserAndRole(this, Role.Participant)),
            instructor = instructorProfile?.toInstructorProfileResponse(userSeminarRepository.findByUserAndRole(this, Role.Instructor)!!)
        )
    }
    fun edit(req: EditUserRequest, passwordEncoder: PasswordEncoder) {
        if(req.username != null) username = req.username
        if(req.password != null) password = passwordEncoder.encode(req.password)
        if(participantProfile != null) {
            participantProfile!!.university = req.university
        }
        if(instructorProfile != null) {
            instructorProfile!!.company = req.company
            if(req.year != null) {
                if(req.year < 0) throw Seminar400("year 정보가 정확하지 않습니다.")
                instructorProfile!!.year = req.year
            }
        }
    }
    
    fun makeParticipant(req: ParticipantRequest) {
        if(participantProfile != null) throw Seminar409("이미 참가자로 등록된 사용자입니다.")
        participantProfile = ParticipantProfile(req.university, req.isRegistered)
    }
}
