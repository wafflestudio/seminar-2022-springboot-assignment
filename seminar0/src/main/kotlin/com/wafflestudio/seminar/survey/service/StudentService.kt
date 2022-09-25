package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepositoryImpl
import com.wafflestudio.seminar.survey.database.SurveyRepositoryImpl
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StudentService {
    
    @Autowired
    private lateinit var surveyRepositoryImpl: SurveyRepositoryImpl
    
    @Autowired
    private lateinit var osRepositoryImpl: OsRepositoryImpl
    
    fun findAllSurvey(): List<SurveyResponse> {
        return surveyRepositoryImpl.findAll()
    }
    
    fun findByIdSurvey(id: Long) : SurveyResponse {
        return surveyRepositoryImpl.findById(id)
    }


    
    fun findAllOs(): List<OperatingSystem> {
        return osRepositoryImpl.findAll()
    }
    
    
    fun findByIdOs(id: Long): OperatingSystem {
        return osRepositoryImpl.findById(id)
    }
    
     
}

 