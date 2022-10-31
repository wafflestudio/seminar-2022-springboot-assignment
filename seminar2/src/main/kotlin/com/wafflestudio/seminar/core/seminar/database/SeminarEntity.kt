package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.core.join.UserSeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import javax.persistence.*

@Entity
@Table(name = "seminar")
class SeminarEntity(
        @Column(nullable = false)
        var name: String,

        var capacity: Int,

        var count: Int,

        var time: Int, // Change to total minutes.

        var online: Boolean,

        @OneToMany(mappedBy = "seminar")
        val users: MutableSet<UserSeminarEntity> = mutableSetOf(),

        @OneToOne
        @JoinColumn(name = "created_user_id")
        val created_user: UserEntity,
) {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}