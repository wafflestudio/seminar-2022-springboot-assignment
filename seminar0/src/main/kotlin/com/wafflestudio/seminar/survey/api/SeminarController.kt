package com.wafflestudio.seminar.survey.apiimport com.wafflestudio.seminar.survey.database.OsRepositoryimport com.wafflestudio.seminar.survey.domain.OperatingSystemimport com.wafflestudio.seminar.survey.domain.SurveyResponseimport com.wafflestudio.seminar.survey.exception.idExceptionimport com.wafflestudio.seminar.survey.exception.osNameExceptionimport com.wafflestudio.seminar.survey.exception.osidExceptionimport com.wafflestudio.seminar.survey.service.SeminarServiceimport org.springframework.beans.factory.annotation.Autowiredimport org.springframework.web.bind.annotation.*@RestControllerclass SeminarController(val seminarService: SeminarService) {    @GetMapping("/surveyall")    fun Listall(): List<SurveyResponse>{        return seminarService.getAllSurvey()    }    @GetMapping("/survey/{id}")    fun getSurveyByID(@PathVariable id: Long): SurveyResponse?{                return seminarService.getSurveyByID(id)?: throw idException()    }    @GetMapping("/os/{id}")    fun getOSByID(@PathVariable id: Long): OperatingSystem?{        return seminarService.getOSByID(id)?: throw osidException()    }    @GetMapping("/os/name")    fun getOSByName(@RequestParam osName: String? = null,): OperatingSystem?{        return seminarService.getOSByName(osName)?: throw osNameException()    }}