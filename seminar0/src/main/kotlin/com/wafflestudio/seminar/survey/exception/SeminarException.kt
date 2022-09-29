package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus

class SeminarException(val errorMessage: String,val httpstatus:HttpStatus) : RuntimeException() {
    
}