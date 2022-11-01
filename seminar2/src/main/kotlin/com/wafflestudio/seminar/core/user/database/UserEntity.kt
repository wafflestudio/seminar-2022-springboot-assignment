package com.wafflestudio.seminar.core.user.database
import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import javax.persistence.*

@Entity
class UserEntity(
    val username: String,
    val email: String,
    val password: String,
    
    @OneToOne(cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "participant_id")
    var participant: ParticipantEntity? = null,
    
    @OneToOne(cascade = [CascadeType.ALL], optional = true)
    @JoinColumn(name = "instructor_id")
    var instructor: InstructorEntity? = null,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val userSeminars: MutableList<UserSeminarEntity> = mutableListOf()
): BaseTimeEntity()