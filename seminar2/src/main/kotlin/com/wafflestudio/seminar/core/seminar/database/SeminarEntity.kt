package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name="seminar")
class SeminarEntity(
    override var createdAt: LocalDateTime? = null,
    override var modifiedAt: LocalDateTime? = null,
    @field:NotBlank @field:NotEmpty @Column(unique = true) var name: String,
    @field:Min(1) var capacity: Int,
    @field:Min(1) var count: Int,
    var time: String,
    var online: Boolean = true,
    
    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.REMOVE])
    var users: MutableList<UserSeminarEntity> = mutableListOf(),
    
    
) : BaseTimeEntity() {
}
