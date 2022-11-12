package com.wafflestudio.seminar.core.user.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.user.type.UserRole
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


@Entity
@Table(name = "seminar_user")
class UserEntity(
    var loginedAt: LocalDateTime,
    @field:NotBlank var username: String,
    @field:NotEmpty @Column(unique = true) @Email var email: String,
    @field:NotNull var password: String,
    @field:NotNull @Enumerated(EnumType.STRING) var role: UserRole,
    var university: String = "",
    var isRegistered: Boolean,
    var company: String = "",
    var year: Int? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    var seminars: MutableList<UserSeminarEntity> = mutableListOf()
) : BaseTimeEntity() {
}
