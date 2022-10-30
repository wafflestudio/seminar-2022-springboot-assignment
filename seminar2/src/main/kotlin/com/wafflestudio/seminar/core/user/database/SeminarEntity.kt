package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import java.time.LocalTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

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
    
    @OneToMany(mappedBy = "seminar")
    private val users: Set<UserEntity> = emptySet()
}