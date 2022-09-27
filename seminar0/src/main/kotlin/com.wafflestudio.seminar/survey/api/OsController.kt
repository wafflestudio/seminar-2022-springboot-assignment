package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/os")
class OsController(private val osService: OsService) {
    @GetMapping
    fun getOsByName(@RequestParam name: String) = osService.searchByName(name)
    @GetMapping("/total")
    fun getTotalOs() = osService.searchTotal()
    @GetMapping("/{id}")
    fun getOsById(@PathVariable id : Long) = osService.searchById(id)
}