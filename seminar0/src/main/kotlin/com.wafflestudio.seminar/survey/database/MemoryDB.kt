package com.wafflestudio.seminar.survey.database

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class MemoryDB : SurveyResponseDB, OsDB {
    private val operatingSystems = mutableListOf<OperatingSystem>()
    private val surveyResponses = mutableListOf<SurveyResponse>()

<<<<<<< HEAD
    

    override fun getOperatingSystems(): List<OperatingSystem> {
        return operatingSystems
    }

    override fun getSurveyResponses(): List<SurveyResponse> {
=======
    fun getOperatingSystems(): List<OperatingSystem> {
        return operatingSystems
    }

    fun getSurveyResponses(): List<SurveyResponse> {
>>>>>>> 70abd32c4e04fc14d4120c219eb493f4add948bc
        return surveyResponses
    }

    override fun getSurveyResponseById(id: Long): SurveyResponse {
        var result: SurveyResponse? = null
        for (surveyResponse in surveyResponses) {
            if (surveyResponse.id == id) {
                result = surveyResponse
                break
            }
        }
        return result ?: throw IllegalArgumentException("id#${id} NOT FOUND")
    }

    override fun getOperatingSystemById(id: Long): OperatingSystem {
        var result: OperatingSystem? = null
        for (operatingSystem in operatingSystems) {
            if (operatingSystem.id == id) {
                result = operatingSystem
                break
            }
        }
        return result ?: throw IllegalArgumentException("ID#${id} NOT FOUND")
    }

    override fun getOperatingSystemByOSName(osName: String): OperatingSystem {
        var result: OperatingSystem? = null
        for (operatingSystem in operatingSystems) {
            if (operatingSystem.osName == osName) {
                result = operatingSystem
                break
            }
        }
        return result ?: throw IllegalArgumentException("OS Name\"${osName}\" NOT FOUND")
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
                    timestamp = LocalDateTime.parse(
                        rawSurveyResponse[0],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    ),
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