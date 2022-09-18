package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/os")
class OsController(private val osService: OsService) {
    @GetMapping("/total")
    fun getTotalOs() = osService.getAllOS()
    @GetMapping
    fun getOsByName(@RequestParam name: String) = osService.searchOSByName(name)
    @GetMapping("/{id}")
    fun getOsById(@PathVariable id : Long) = osService.searchOSById(id)
}