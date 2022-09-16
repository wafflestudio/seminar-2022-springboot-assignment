package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.database.MemoryDB
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import javax.annotation.PostConstruct


@RestController
@RequestMapping("survey")
class HomeController(private val studentService : StudentService) {
    
    @GetMapping()
    fun Index() = "Hello"
    
    
    @GetMapping("/all")
    fun findAllSurvey() : String {
        var list : List<SurveyResponse> = studentService.findAllSurvey()
        return list.toString()
    }
    
    @GetMapping("/{id}")
    fun findByIdSurvey(@PathVariable id:Long) : String {
        var surveyResponse : SurveyResponse = studentService.findByIdSurvey(id)
        return surveyResponse.toString()
    }
    
    @GetMapping("/os/{osId}")
    fun findByOsId(@PathVariable osId:Long) : String {
        var os: OperatingSystem = studentService.findByIdOs(osId)

        var list : List<SurveyResponse> = studentService.findAllSurvey()
        var result = list.filter { it.operatingSystem == os }
        
        return result.toString()
    }
    
    @GetMapping("/os")
    fun findByOsName(@RequestParam("osName") osName: String) :String {
        lateinit var os: OperatingSystem
        when(osName) {
            "MacOS" -> os = studentService.findByIdOs(1)
            "Linux" -> os = studentService.findByIdOs(2)
            "Windows" -> os = studentService.findByIdOs(3)
        }

        var list : List<SurveyResponse> = studentService.findAllSurvey()
        var result = list.filter { it.operatingSystem == os }
        
        return result.toString()
    }
    
   
    
}