package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/os")
class OsController(private val osService: OsService) {
    @GetMapping("/{osId}")
    fun getByOsId(@PathVariable osId : Long):OperatingSystem{
        return osService.findById(osId)
    }
    
    @GetMapping
    fun getByOsName(@RequestParam name:String):OperatingSystem{
        return osService.findByName(name)
    }
}