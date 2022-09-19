package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.OsDB
import com.wafflestudio.seminar.survey.service.OsService
import com.wafflestudio.seminar.survey.service.SurveyDB
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class MemoryDB: OsDB, SurveyDB {
    private val operatingSystems = mutableListOf<OperatingSystem>()
    private val surveyResponses = mutableListOf<SurveyResponse>()

    override fun getOperatingSystems(): List<OperatingSystem> {
        return operatingSystems
    }
    
    override fun getSurveyResponses(): List<SurveyResponse> {
        return surveyResponses
    }
    
    /**
     * 서버가 시작하면, 엑셀 파일을 메모리로 불러오는 역할을 수행해요
     * 지금 당장 이해할 필요는 없어요!
     */
    @EventListener
    fun loadExcel(event: ApplicationStartedEvent) {
        loadOS()
        loadSurveyResponses()
    }

    private fun loadOS() = operatingSystems.addAll(
        listOf(
            OperatingSystem(1L, "MacOS", 300000L, "Most favorite OS of Seminar Instructors"),
            OperatingSystem(2L, "Linux", 0L, "Linus Benedict Torvalds"),
            OperatingSystem(3L, "Windows", 0L, "Window.."),
        )
    )

    private fun loadSurveyResponses() {
        val responses = ClassPathResource("data/example_surveyresult.tsv")
            .file
            .readLines()
            .mapIndexed { idx, it ->
                val rawSurveyResponse = it.split("\t")
                SurveyResponse(
                    id = idx.toLong(),
                    timestamp = LocalDateTime.parse(rawSurveyResponse[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    operatingSystem = operatingSystems.find { os -> os.osName == rawSurveyResponse[1] }!!,
                    springExp = rawSurveyResponse[2].toInt(),
                    rdbExp = rawSurveyResponse[3].toInt(),
                    programmingExp = rawSurveyResponse[4].toInt(),
                    major = rawSurveyResponse[5],
                    grade = rawSurveyResponse[6],
                )
            }

        surveyResponses.addAll(responses)
    }
}