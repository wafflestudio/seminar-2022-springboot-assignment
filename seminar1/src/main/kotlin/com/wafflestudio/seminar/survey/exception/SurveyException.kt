package com.wafflestudio.seminar.survey.exception

import java.lang.RuntimeException

open class SurveyException (
    val errorCode: ErrorCode
        ) : java.lang.RuntimeException()


class InCorrectInputException : SurveyException(ErrorCode.INCORRECT_INPUT)

class InCorrectOsNameException : SurveyException(ErrorCode.INCORRECT_OSNAME)

class AlreadyExistsSurveyException : SurveyException(ErrorCode.ALREADY_EXISTS_SURVEY)

