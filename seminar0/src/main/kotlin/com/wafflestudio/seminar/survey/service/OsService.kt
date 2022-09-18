package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem

interface OsService {
    fun findAll(): List<OperatingSystem>
    fun findById(id: Long): OperatingSystem?
    fun findByName(name: String): OperatingSystem? 
}