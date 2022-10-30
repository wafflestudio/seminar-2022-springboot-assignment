package com.wafflestudio.seminar.core.seminar.repository

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.mappingTable.UserSeminar
import com.wafflestudio.seminar.core.seminar.api.request.SeminarMakeRequest
import com.wafflestudio.seminar.core.user.repository.UserEntity
import javax.persistence.*

@Entity
class Seminar(
    var name: String,
    var capacity: Int,
    var count: Int,
    var time: String,
    var online: Boolean = true,
): BaseTimeEntity() {
    
    constructor(request: SeminarMakeRequest) : this(
        request.name, 
        request.capacity,
        request.count,
        request.time,
        request.online
    )
    
    @OneToMany(
        mappedBy = "seminar",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST, CascadeType.REMOVE],
        orphanRemoval = true
    )
    val userSeminars: MutableList<UserSeminar> = mutableListOf()
    
    var participantCount: Int = 0;
}