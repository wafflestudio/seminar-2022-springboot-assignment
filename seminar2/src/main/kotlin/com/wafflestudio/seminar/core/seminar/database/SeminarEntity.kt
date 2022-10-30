package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.core.jointable.UserSeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import java.time.LocalDateTime
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "seminar")
class SeminarEntity(
        @Column(nullable = false)
        val name: String,
        
        val capacity: Int,
        
        var count: Int,
        
        val time: LocalDateTime,
        
        val online: Boolean,
        
        @OneToMany(mappedBy = "seminar")
        val users: MutableSet<UserSeminarEntity> = mutableSetOf()
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}