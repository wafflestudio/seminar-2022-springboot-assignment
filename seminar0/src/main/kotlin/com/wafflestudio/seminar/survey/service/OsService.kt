package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.OptionalDouble

@Service
class OsService (val osRepository: OsRepository){
    fun findById(id: Long): OperatingSystem{
        return osRepository.findById(id)
    }
    fun findByName(name :String): List<OperatingSystem>{
        return osRepository.findByName(name)
    }
}