package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/")
class OsController(
    private val osService: OsService,
    private val seminarExceptionHandler: SeminarExceptionHandler
) {
    @GetMapping("os/")
    fun getAllOs():List<OperatingSystem>{
        return osService.getAllOs()
    }
    @GetMapping("os")
    fun getOsByName(@RequestParam name:String): OperatingSystem {
        
        return try{
            osService.getOsByName(name)
        }catch (e:RuntimeException){
            throw seminarExceptionHandler.OsNotFoundException()
        }
        
    }
    @GetMapping("os/{id}/")
    fun geyResponseById(@PathVariable("id") id:Long):OperatingSystem{
        
            return try{
                osService.getOsById(id)
            }catch (e:RuntimeException){
                throw seminarExceptionHandler.OsNotFoundException()
            }
    }
    
}