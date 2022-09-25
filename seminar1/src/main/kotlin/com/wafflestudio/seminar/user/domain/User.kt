package com.wafflestudio.seminar.user.domain

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Entity
@Table(name = "seminar_user")
class User(
    @field:NotBlank var name: String,
    @field:NotEmpty @Column(unique = true) @Email var email: String,
    @field:NotNull var password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
}