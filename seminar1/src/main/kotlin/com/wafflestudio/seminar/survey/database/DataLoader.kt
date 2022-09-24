package com.wafflestudio.seminar.survey.database

import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * MemoryDB는 이제, DataLoader 라는 이름으로 바뀌었어요.
 * 이제는 메모리가 아닌 원격 저장소를 사용할 거에요.
 * 데이터 로더는, 엑셀 결과를 데이터베이스로 옮기는 역할을 수행해요.
 */
@Component
class DataLoader(
    private val osRepository: OsRepository,
    private val surveyResponseRepository: SurveyResponseRepository,
) {

    /**
     * 서버가 시작하면, 엑셀 파일을 메모리로 불러오는 역할을 수행해요
     * 지금 당장 이해할 필요는 없어요!
     */
    @EventListener
    fun loadExcel(event: ApplicationStartedEvent) {
        loadOS()
        loadSurveyResponses()
    }

    private fun loadOS() {
        if (osRepository.findAll().isNotEmpty()) {
            return
        }

        osRepository.saveAll(
            listOf(
                OperatingSystemEntity("MacOS", 300000L, "Most favorite OS of Seminar Instructors"),
                OperatingSystemEntity("Linux", 0L, "Linus Benedict Torvalds"),
                OperatingSystemEntity("Windows", 0L, "Window.."),
            )
        )
    }

    private fun loadSurveyResponses() {
        if (surveyResponseRepository.findAll().isNotEmpty()) {
            return
        }

        val responses = ClassPathResource("data/example_surveyresult.tsv")
            .file
            .readLines()
            .map {
                val rawSurveyResponse = it.split("\t")
                SurveyResponseEntity(
                    timestamp = LocalDateTime.parse(rawSurveyResponse[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    operatingSystem = osRepository.findByOsName(rawSurveyResponse[1])!!,
                    springExp = rawSurveyResponse[2].toInt(),
                    rdbExp = rawSurveyResponse[3].toInt(),
                    programmingExp = rawSurveyResponse[4].toInt(),
                    major = rawSurveyResponse[5],
                    grade = rawSurveyResponse[6],
                )
            }

        surveyResponseRepository.saveAll(responses)
    }
}