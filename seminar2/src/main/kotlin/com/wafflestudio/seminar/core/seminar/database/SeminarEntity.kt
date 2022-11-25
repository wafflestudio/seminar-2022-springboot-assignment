package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Entity
@Table(name="seminar")
class SeminarEntity(
    @field:NotBlank @field:NotEmpty @Column(unique = true) var name: String,
    @field:Min(1) var capacity: Int,
    @field:Min(1) var count: Int,
    var time: String,
    var online: Boolean = true,

    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var userSeminars: MutableList<UserSeminarEntity> = mutableListOf(),


    ) : BaseTimeEntity() {
    
    fun update(request: UpdateSeminarRequest) {
        this.name = request.name ?: this.name
        this.capacity = request.capacity ?: this.capacity
        this.count = request.count ?: this.count
        this.time = request.time ?: this.time
        this.online = request.online ?: this.online
    }
}
