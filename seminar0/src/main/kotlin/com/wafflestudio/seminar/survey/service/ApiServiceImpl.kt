package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class ApiServiceImpl(
    private val osRepository: OsRepository,
    private val surveyResponseRepository: SurveyResponseRepository
    ):ApiService {
    
    override fun findOSbyId(id:Long):OperatingSystem?{
        return osRepository.findById(id)
    }
    
    override fun findOSbyName(name:String):OperatingSystem?{
        return osRepository.findbyName(name)
    }
    
    override fun findSurveyResponsebyId(id:Long):SurveyResponse?{
        return surveyResponseRepository.findById(id)
    }
    
    override fun findAllSurveyResponse():List<SurveyResponse>{
        return surveyResponseRepository.findAll()
    }
    
}