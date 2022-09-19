package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
interface OsService {
    fun findById(id : Long) : OperatingSystem?
    fun findByName(name : String) : OperatingSystem?
}