package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name="UserSeminar")
class UserSeminarEntity(
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "user_id")
    val user: UserEntity? = null,

    @Column(name="is_instructor")
    val isInstructor: Boolean,
    
    @Column(name="is_active")
    var isActive: Boolean? = true,
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REMOVE])
    @JoinColumn(name = "seminar_id")
    val seminar: SeminarEntity
    ): BaseTimeEntity() {
    
    override var modifiedAt: LocalDateTime?
        get() = super.modifiedAt
        set(value) {
            modifiedAt = value
        }
}