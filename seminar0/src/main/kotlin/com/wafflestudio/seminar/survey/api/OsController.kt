package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.exception.SeminarException
import com.wafflestudio.seminar.survey.exception.SeminarExceptionCode
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/os")
class OsController(
        private val osService: OsService
        
) {
   @GetMapping("/all")
   fun getAll(): List<OperatingSystem> {
      return osService.findAll()
   }

   @GetMapping("/id={id}")
   fun getById(@PathVariable("id") id: Long): OperatingSystem? {
      return osService.findById(id) ?: throw SeminarException(SeminarExceptionCode.NoOSError)
   }

   @GetMapping("/name={name}")
   fun getByName(@PathVariable("name") name: String): OperatingSystem? {
      return osService.findByName(name) ?: throw SeminarException(SeminarExceptionCode.NoOSError)
   }
   
}