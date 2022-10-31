package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.UserSeminar
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.util.*
import javax.persistence.*

@Entity
class UserSeminarEntity : BaseTimeEntity() {
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "SEMINAR_ID")
    lateinit var seminar : SeminarEntity

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "USER_ID")
    lateinit var user : UserEntity
    
    fun toUserSeminar(){
        UserSeminar(
            id = seminar.id,
            name = seminar.name,
            joinedAt = Date(), // this.createdAt
            isActive = true,
            droppedAt = Date()
        )
    }
    
}