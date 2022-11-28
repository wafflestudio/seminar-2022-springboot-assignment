package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarEntity
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "seminars")
class SeminarEntity(
    var name: String,
    var capacity: Int,
    var count: Int,
    var time: String,
    var online: Boolean = true,
    var managerId: Long,
    @CreationTimestamp
    override var createdAt: LocalDateTime? = LocalDateTime.now(),
    @CreationTimestamp
    override var modifiedAt: LocalDateTime? = createdAt
) : BaseTimeEntity() {

    @OneToMany(mappedBy = "seminarEntity", cascade = [CascadeType.REMOVE])
    var userSeminarEntities: MutableList<UserSeminarEntity> = mutableListOf()  
    var participantCount: Long = 0
}