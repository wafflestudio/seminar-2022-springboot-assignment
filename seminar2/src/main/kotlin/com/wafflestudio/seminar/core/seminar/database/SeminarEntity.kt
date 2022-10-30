package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany

@Entity
class SeminarEntity(
    var name: String,
    var capacity: Int,
    var count: Int,
    var time: LocalTime,
    var online: Boolean,
    creator: UserEntity,
): BaseTimeEntity() {
    
    val creatorId = creator.id
    
    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val userSeminars: MutableList<UserSeminarEntity> = mutableListOf()
    
    init {
        addInstructor(creator)
    }
    
    fun update(userEntity: UserEntity, request: UpdateSeminarRequest) {
        if (creatorId != userEntity.id) {
            throw Seminar403("수정을 할 수 없습니다.")
        }
        
        this.count = request.count
        this.capacity = request.capacity
        this.name = request.name
        this.time = request.time
        this.online = request.online
    }
    
    final fun addInstructor(userEntity: UserEntity) {
        requireNotNull(userEntity.instructorProfile) {
            throw Seminar403("${userEntity.id} 유저는 강사가 될 수 없습니다.")
        }
        
        val relation = UserSeminarEntity.instructor(userEntity.id, this)
        userSeminars.add(relation)
    }
    
    fun addParticipant(userEntity: UserEntity) {
        requireNotNull(userEntity.participantProfile) {
            throw Seminar403("${userEntity.id} 유저의 수강생 정보가 없습니다.")
        }
        
        val relation = UserSeminarEntity.participant(userEntity.id, this)
        userSeminars.add(relation)
    }
}