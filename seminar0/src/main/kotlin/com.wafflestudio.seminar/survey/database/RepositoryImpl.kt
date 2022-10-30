package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Component

@Component
class SurveyRepositoryImpl(
        private val db: MemoryDB
): SurveyResponseRepository {
    override fun findAll(): List<SurveyResponse> {
        return db.getSurveyResponses()
    }
    override fun SfindById(id: Long): SurveyResponse? {
        val a = db.getSurveyResponses()
        for (i in a){
            if (i.id == id){
                return i
            }
        }
        return null
    }
}

@Component
class OsRepositoryImpl(
        private val db: MemoryDB
): OsRepository {
    override fun OfindById(id: Long): OperatingSystem? {
        val a = db.getOperatingSystems()
        for (i in a){
            if (i.id == id){
                return i
            }
        }
        return null
    }
    override fun findByOsName(osName: String): OperatingSystem? {
        val a = db.getOperatingSystems()
        for (i in a){
            if (i.osName == osName){
                return i
            }
        }
        return null
    }
}