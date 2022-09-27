package com.wafflestudio.seminar.survey.api.exception

import org.springframework.http.HttpStatus


enum class ErrorCode(status: HttpStatus, message: String) {
    //400 BAD_REQUEST
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "파라미터 값을 확인해 주세요"),
    // 404 NOT_FOUND
    Survey_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Survey ID에 해당하는 데이터가 존재하지 않습니다. \n잘못된 ID:"),
    OS_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "OS ID에 해당하는 데이터가 존재하지 않습니다. \n잘못된 ID:"),
    OS_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "OS Name에 해당하는 데이터가 존재하지 않습니다. \n잘못된 Name:");
    
    public val status : HttpStatus = status
    public val message : String = message
}