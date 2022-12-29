package com.wafflestudio.seminar.user.database

import javax.persistence.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
class UserEntity (
    @NotEmpty val name: String,
    @NotEmpty val email: String,
    val password: String,
) {
    @Id
    @GeneratedValue
    val id: Long = 0L
}