package com.wafflestudio.seminar.survey.service

import org.springframework.stereotype.Service

@Service
class Service(
    private val surveyResponseRepositoryImpl: SurveyResponseRepositoryImpl,
    private val osRepositoryImpl: OsRepositoryImpl
) {
    fun getSurveyResponseAll(): String {
        return surveyResponseRepositoryImpl.findAll().joinToString("\n", "[\n", "\n]");
    }
    
    fun getSurveyResponseById(id: Long): String {
        return surveyResponseRepositoryImpl.findById(id).toString();
    }
    
    fun getOSById(id: Long): String {
        return osRepositoryImpl.findById(id).toString();
    }
    
    fun getOSByOSName(osName: String): String {
        return osRepositoryImpl.findByOSName(osName).toString();
    }
}