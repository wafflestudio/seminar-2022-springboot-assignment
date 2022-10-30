package com.wafflestudio.seminar.core.mappingTable

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.repository.Seminar
import com.wafflestudio.seminar.core.user.Role
import com.wafflestudio.seminar.core.user.repository.UserEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class UserSeminar(
    seminar: Seminar,
    userEntity: UserEntity,
    role: Role
): BaseTimeEntity() {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id")
    var seminar: Seminar = seminar

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity = userEntity
    
    @Enumerated(EnumType.STRING)
    val role = role
    
    var isActive: Boolean = true;
    var droppedAt: LocalDateTime? = null
    
    fun dropSeminar() {
        isActive = false;
        droppedAt = LocalDateTime.now()
        seminar.participantCount -= 1
    }
}