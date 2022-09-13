package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class OsService (
    private val osRepository: OsRepository
    ){
        fun getAllOs():List<OperatingSystem>{
            return osRepository.findAll()
        }
        fun getOsById(id:Long):OperatingSystem{
            return osRepository.findById(id)
        }
}
