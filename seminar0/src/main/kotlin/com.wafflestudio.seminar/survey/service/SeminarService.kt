package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.SeminarException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class Service(private val osRepository : OsRepository, private val surveyResponseRepository : SurveyResponseRepository) {
    fun findByOsId(id: Long): OperatingSystem {
        if(osRepository.findById(id)!=null){return osRepository.findById(id)!!}
        throw SeminarException("Cannot find the os with the id of $id", HttpStatus.BAD_REQUEST)
    }

    fun findByOsName(name: String): OperatingSystem {
        var totalList: MutableList<OperatingSystem> = osRepository.findAll().toMutableList()
        totalList.forEach{
            if(it.osName.equals(name)){
                return it
            }
        }
        throw SeminarException("Cannot find the os with the name of $name", HttpStatus.BAD_REQUEST)
    }

    fun findAllSurveyResponse(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    fun findBySurveyResponseId(id: Long): SurveyResponse {
        if(surveyResponseRepository.findById(id)!=null){
            return surveyResponseRepository.findById(id)!!
        }
        throw SeminarException("Cannot find the survey response with the id of $id", HttpStatus.BAD_REQUEST)
    }
}