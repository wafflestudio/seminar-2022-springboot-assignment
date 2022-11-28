package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "participantProfiles")
class ParticipantProfileEntity(
    @Column
    var university: String,
    @Column
    val isRegistered: Boolean,
    @CreationTimestamp
    override var createdAt: LocalDateTime? = LocalDateTime.now(),
    @CreationTimestamp
    override var modifiedAt: LocalDateTime? = createdAt,

) : BaseTimeEntity() {

    @OneToOne(mappedBy = "participantProfileEntity", fetch = FetchType.LAZY)
    var userEntity: UserEntity? = null
    

    

}