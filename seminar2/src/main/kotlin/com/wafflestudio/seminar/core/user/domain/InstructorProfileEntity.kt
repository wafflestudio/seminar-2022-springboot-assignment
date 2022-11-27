package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.user.dto.auth.InstructorProfileDto
import javax.persistence.*

@Entity
@Table(name = "InstructorProfiles")
class InstructorProfileEntity(
        
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        var user: UserEntity? = null,

        @Column
        var company: String? = "",

        @Column(name = "instructing_year")
        var year: Int? = null,


) : BaseTimeEntity() {
    companion object {
        fun of(user: UserEntity?, instructorProfileDto: InstructorProfileDto): InstructorProfileEntity {
            user.run {
                return InstructorProfileEntity(
                        user = user,
                        company = instructorProfileDto.company,
                        year = instructorProfileDto.year
                )
            }
        }
    }
}