package com.wafflestudio.seminar.survey.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus, val message:String){
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "입력값에 맞는 데이터가 존재하지 않습니다.")
}