package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.api.response.GetSeminarInfo
import com.wafflestudio.seminar.core.user.dto.seminar.StudentDto
import javax.persistence.*

@Entity
@Table(name="seminar")
class SeminarEntity(
    
 
    @Column
    var name: String?,
    
    @Column
    var capacity: Int?,
    
    @Column
    var count: Int?,

    @Column
    var time: String?,

    @Column
    var online: Boolean? = true,

    @OneToMany(fetch = FetchType.LAZY, mappedBy="seminar", orphanRemoval = true)
    var userSeminars: MutableList<UserSeminarEntity>?= null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun toSeminar(): GetSeminarInfo {
        val newList = mutableListOf<StudentDto>()
        return GetSeminarInfo(
                id = id,
                name = name,
                capacity = capacity,
                count = count,
                time = time,
                online = online,
                instructors = null,
                participants = null
        )
    }
}