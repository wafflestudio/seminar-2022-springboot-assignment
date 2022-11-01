package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import javax.persistence.*

@Entity
class SeminarEntity (
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: String,
    val online: Boolean? = true,
    
    @OneToMany(mappedBy="seminar", cascade = [CascadeType.ALL])
    val userSeminars: MutableList<UserSeminarEntity> = mutableListOf()
): BaseTimeEntity()