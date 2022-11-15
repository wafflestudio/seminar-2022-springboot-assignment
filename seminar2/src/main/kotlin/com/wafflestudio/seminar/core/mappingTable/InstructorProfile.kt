package com.wafflestudio.seminar.core.mappingTable

import com.wafflestudio.seminar.common.BaseTimeEntity
import javax.persistence.Entity

@Entity
class InstructorProfile(
    var company: String,
    var years: Int?,
): BaseTimeEntity()