package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserSeminarEntity
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "seminar")
data class SeminarEntity(
    @Column(name = "name")
    val name: String,
    @Column(name = "capacity")
    val capacity: Int,
    @Column(name = "count")
    val count: Int,
    @Column(name = "time")
    val time: LocalTime,
    @Column(name = "online")
    val online: Boolean,
): BaseTimeEntity() {
    
    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY)
    private val users: MutableSet<UserSeminarEntity> = mutableSetOf()
}