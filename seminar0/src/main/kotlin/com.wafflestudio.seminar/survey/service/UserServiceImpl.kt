package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.SeminarExceptionHandler
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
        private val surveyResponseRepository: SurveyResponseRepository,
        private val osRepository: OsRepository,
): UserService {
    override fun showAll(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }
    override fun showSurveyById(id: Long): SurveyResponse {
        return surveyResponseRepository.SfindById(id)?: throw SeminarExceptionHandler().SeminarException("USER#$id NOT FOUND!")
    }
    override fun showOsById(id: Long): OperatingSystem {
        return osRepository.OfindById(id)?: throw SeminarExceptionHandler().SeminarException("OS ID#$id NOT FOUND!")
    }
    override fun showOsByOsName(osName: String): OperatingSystem {
        return osRepository.findByOsName(osName)?: throw SeminarExceptionHandler().SeminarException("$osName NOT FOUND!")
    }
}