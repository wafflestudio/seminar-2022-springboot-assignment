package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/os")
class OsController(
    private val os: OsRepository,
) {
    
    @GetMapping("/all")
    fun getOsAll(): List<OperatingSystem>{
        return os.findAll()
    }
    
    @GetMapping("/id")
    fun getOSbyID(
        @RequestParam id: Long
    ): OperatingSystem{
        return os.findById(id)
    }
    
    @GetMapping("/name")
    fun getOSbyName(
        @RequestParam name: String
    ): OperatingSystem{
        return os.findByName(name)
    }
}