package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem


interface OsService {
    fun findByName(name: String): OperatingSystem
    fun findById(id: Long): OperatingSystem
}