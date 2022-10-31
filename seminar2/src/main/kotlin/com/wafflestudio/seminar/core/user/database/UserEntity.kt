package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarEntity
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    var username: String,
    @Column(unique = true)
    val email: String,
    var password: String,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE, CascadeType.PERSIST])
    @JoinColumn(name = "participantProfileEntityId")
    var participantProfileEntity: ParticipantProfileEntity? = null,
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE, CascadeType.PERSIST])
    @JoinColumn(name = "instructorProfileEntityId")
    var instructorProfileEntity: InstructorProfileEntity? = null,
    @CreationTimestamp
    override var createdAt: LocalDateTime? = LocalDateTime.now(),
    @CreationTimestamp
    override var modifiedAt: LocalDateTime? = createdAt,

) : BaseTimeEntity() {
    
    lateinit var role: String
    
    @OneToMany(mappedBy = "userEntity", cascade = [CascadeType.REMOVE])
    var userSeminarEntities: MutableList<UserSeminarEntity> = mutableListOf()

    

}