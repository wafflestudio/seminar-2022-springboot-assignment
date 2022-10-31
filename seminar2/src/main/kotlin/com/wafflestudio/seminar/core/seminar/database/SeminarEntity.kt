package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import java.time.LocalTime
import javax.persistence.*
import javax.transaction.Transactional

@Entity
@Table(name = "seminar")
data class SeminarEntity(
    @Column(name = "name")
    var name: String,
    @Column(name = "capacity")
    var capacity: Int,
    @Column(name = "count")
    var count: Int,
    @Column(name = "time")
    var time: LocalTime,
    @Column(name = "online")
    var online: Boolean = true,
    @Column(name = "hostId", unique = true)
    val hostId: Long,
): BaseTimeEntity() {
    
    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    private val users: MutableSet<UserSeminarEntity> = mutableSetOf()
    
    fun toSeminar(): Seminar {
        return Seminar(
            id = id,
            name = name,
            capacity = capacity,
            count = count,
            time = time,
            online = online,
            hostId = hostId
        )
    }

    @Transactional
    fun update(seminarRequest: EditSeminarRequest) {
        seminarRequest.name?.let {
            name = seminarRequest.name
        }
        seminarRequest.capacity?.let {
            capacity = seminarRequest.capacity
        }
        seminarRequest.count?.let {
            count = seminarRequest.count
        }
        seminarRequest.time?.let {
            time = seminarRequest.time
        }
        seminarRequest.online?.let {
            online = seminarRequest.online
        }
    }
}