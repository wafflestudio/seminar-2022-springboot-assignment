package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.database.MemoryDB
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.StudentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.lang.NumberFormatException
import java.time.LocalDateTime
import javax.annotation.PostConstruct


@RestController
@RequestMapping("survey")
class HomeController(private val studentService : StudentService) {

    @ExceptionHandler(NumberFormatException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleNumberFormat(): String {
        return "Bad Request - NumberFormatException"
    }
    
    @ExceptionHandler(IndexOutOfBoundsException::class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleIndexOutOfBounds(): String {
        return "Internal Server Error - IndexOutOfBoundsException"
    }
    
    @ExceptionHandler(UninitializedPropertyAccessException::class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleUninitializedPropertyAccess(): String {
        return "Internal Server Error - UninitializedPropertyAccessException"
    }
    
    
    
    
    
    @GetMapping()
    fun Index() = "Hello"
    
    
    @GetMapping("/all")
    fun findAllSurvey() : List<SurveyResponse> {
        var list : List<SurveyResponse> = studentService.findAllSurvey()
        return list
    }
    
    @GetMapping("/{id}")
    fun findByIdSurvey(@PathVariable id:Long) : SurveyResponse {
        var surveyResponse : SurveyResponse = studentService.findByIdSurvey(id)
        return surveyResponse
    }
    
    @GetMapping("/os/{osId}")
    fun findByOsId(@PathVariable osId:Long) : List<SurveyResponse> {
        var os: OperatingSystem = studentService.findByIdOs(osId)

        var list : List<SurveyResponse> = studentService.findAllSurvey()
        var result = list.filter { it.operatingSystem == os }
        
        return result
    }
    
    @GetMapping("/os")
    fun findByOsName(@RequestParam("osName") osName: String) :List<SurveyResponse> {
        lateinit var os: OperatingSystem
        when(osName) {
            "MacOS" -> os = studentService.findByIdOs(1)
            "Linux" -> os = studentService.findByIdOs(2)
            "Windows" -> os = studentService.findByIdOs(3)
           
        }

        var list : List<SurveyResponse> = studentService.findAllSurvey()
        var result = list.filter { it.operatingSystem == os }
        
        return result
    }
    
   
    
}