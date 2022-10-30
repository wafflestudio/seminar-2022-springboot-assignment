package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Entity

@Entity
class SeminarEntity(
    val name: String,
): BaseTimeEntity()