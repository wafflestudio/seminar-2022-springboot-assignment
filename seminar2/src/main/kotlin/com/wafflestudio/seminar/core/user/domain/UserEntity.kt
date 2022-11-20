package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(

        @Column
        var username: String?,

        @Column
        var email: String?,

        @Column
        var password: String?,

//        @Column
//        var dateJoined: LocalDate?,

//        @Column
//        var lastLogin: LocalDate? = null,

        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
        @JoinColumn(name = "participant_id")
        var participant: ParticipantProfileEntity? = null,

        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
        @JoinColumn(name = "instructor_id")
        var instructor: InstructorProfileEntity? = null,

        @OneToMany(mappedBy = "user", orphanRemoval = true)
        var userSeminars: MutableList<UserSeminarEntity>? = null

) : BaseTimeEntity() {

        @Column
        var dateJoined: LocalDate? = LocalDate.now()

        @Column
        var lastLogin: LocalDate? = null
        companion object {
        fun of(request: SignUpRequest, password: String?) :UserEntity {
            request.run { 
                return UserEntity(
                        username = request.username,
                        email = request.email,
                        password = password,
                        if(request.role == "PARTICIPANT" && request.participant != null) {
                                ParticipantProfileEntity(request.participant.university, request.participant.isRegistered)
                        } else null,
                        if(request.role == "INSTRUCTOR" && request.instructor != null) {
                                InstructorProfileEntity(request.instructor.company, request.instructor.year)
                        } else null, 
                        mutableListOf()
                )
            }
        }
    }

}