package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.database.SurveyResponseRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.exception.OSNotFoundException
import com.wafflestudio.seminar.survey.exception.SurveyResultNotFoundException
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(
    private val osRepository: OsRepository,
    private val surveyResponseRepository: SurveyResponseRepository,
):SearchService {
    override fun getOsById(id: Long): OperatingSystem {
        return osRepository.findById(id) ?: throw OSNotFoundException("OS$id NOT FOUND!")
    }

    override fun getOsByName(name: String): OperatingSystem {
        return osRepository.findByName(name) ?: throw OSNotFoundException("OS$name NOT FOUND!")
    }

    override fun getAllSurveyResponses(): List<SurveyResponse> {
        return surveyResponseRepository.findAll()
    }

    override fun getSurveyResponseById(id: Long): SurveyResponse {
        return surveyResponseRepository.findById(id) ?: throw SurveyResultNotFoundException("SurveyResponse$id NOT FOUND!")
    }
}