package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "instructorProfiles")
class InstructorProfileEntity(
    @Column
    var company: String,
    @Column
    var year: Int?,
    @CreationTimestamp
    override var createdAt: LocalDateTime? = LocalDateTime.now(),
    @CreationTimestamp
    override var modifiedAt: LocalDateTime? = createdAt,

) : BaseTimeEntity() {

    @OneToOne(mappedBy = "instructorProfileEntity")
    var userEntity: UserEntity? = null

    

}