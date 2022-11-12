package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.common.BaseTimeEntity
import java.time.LocalDate
import javax.persistence.*

@Entity
//@Table(name="user")
class UserEntity(

    @Column
    var username: String?,

    @Column
    var email: String?,

    @Column
    var password: String?,

    @Column
    var dateJoined: LocalDate?,

    @Column
    var lastLogin: LocalDate? = null,

    @OneToOne(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name="participant_id")
    var participant: ParticipantProfileEntity? = null,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name="instructor_id")
    var instructor: InstructorProfileEntity? = null,

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    var userSeminars :MutableList<UserSeminarEntity>? = null
    
):BaseTimeEntity() {
   
}