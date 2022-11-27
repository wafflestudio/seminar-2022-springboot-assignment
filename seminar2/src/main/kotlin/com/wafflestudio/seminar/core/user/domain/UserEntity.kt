package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "Users")
class UserEntity(

        @Column
        var username: String?,

        @Column
        var email: String?,

        @Column
        var password: String?,

        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinColumn(name = "participant_id", referencedColumnName = "id")
        var participant: ParticipantProfileEntity? = null,

        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinColumn(name = "instructor_id", referencedColumnName = "id")
        var instructor: InstructorProfileEntity? = null,

        @OneToMany(mappedBy = "user", orphanRemoval = true)
        var userSeminars: MutableList<UserSeminarEntity>? = null

) : BaseTimeEntity() {

    @Column
    var dateJoined: LocalDate? = LocalDate.now()

    @Column
    var lastLogin: LocalDate? = null

    companion object {
        fun of(request: SignUpRequest, password: String?): UserEntity {
            request.run {
                return UserEntity(
                        username = request.username,
                        email = request.email,
                        password = password,
                        null,
                         null,
                        mutableListOf()
                )
            }
        }
    }

}