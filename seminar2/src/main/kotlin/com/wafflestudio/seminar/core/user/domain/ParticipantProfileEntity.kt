package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.dto.auth.ParticipantProfileDto
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "ParticipantProfiles")
class ParticipantProfileEntity(

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        var user: UserEntity? = null,

        @Column
        var university: String? = "",

        @Column
        var isRegistered: Boolean? = true,

       
        
        
) : BaseTimeEntity() {

    companion object {
        fun of(user: UserEntity?, participantProfileDto: ParticipantProfileDto): ParticipantProfileEntity {
            user.run {
                return ParticipantProfileEntity(
                        user = user,
                        university = participantProfileDto.university,
                        isRegistered = participantProfileDto.isRegistered
                )
            }
        }
    }
}