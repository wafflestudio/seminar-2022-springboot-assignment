package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

interface OSService {
    fun osForName(name: String): OperatingSystem
    fun osForId(id: Long): OperatingSystem

}

@Service
class DefaultOSService(
    val repository: OsRepository
): OSService {
    
    override fun osForName(name: String): OperatingSystem {
        return repository.findByName(name)
    }

    override fun osForId(id: Long): OperatingSystem {
        return repository.findById(id)
    }

}

interface SurveyService {
    fun allSurveyList(): List<SurveyResponse>
    fun surveyForId(id: Long): SurveyResponse
}

@Service
class DefaultSurveyService(
    val repository: SurveyResponseRepository
): SurveyService {
    override fun allSurveyList(): List<SurveyResponse> {
        return repository.findAll()
    }

    override fun surveyForId(id: Long): SurveyResponse {
        return repository.findById(id)
    }
}