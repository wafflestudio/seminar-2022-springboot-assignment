package com.wafflestudio.seminar.common

annotation class Authenticated

annotation class UserContext

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogExecutionTime