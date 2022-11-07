package com.wafflestudio.seminar.core.userseminar.database

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.apache.catalina.User
import org.hibernate.query.criteria.internal.expression.function.SubstringFunction
import org.springframework.data.jpa.repository.JpaRepository

interface UserSeminarRepository : JpaRepository<UserSeminarEntity, Long> {
    fun findByUserAndSeminarAndRole(user: UserEntity, seminar: SeminarEntity, role: String) : List<UserSeminarEntity>
    fun findByUserAndRole(user: UserEntity, role: String) : List<UserSeminarEntity>
    fun findBySeminar(seminar: SeminarEntity) : List<UserSeminarEntity>
    fun findBySeminarAndRole(seminar: SeminarEntity, role: String) : List<UserSeminarEntity>
    fun findByUserAndSeminar(user: UserEntity, seminar: SeminarEntity) : List<UserSeminarEntity>
}