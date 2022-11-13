package com.wafflestudio.seminar.common

import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


annotation class Authenticated

annotation class UserContext



// For Enum type validation
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [EnumPatternValidator::class])
annotation class EnumPattern(
    val enumClass: KClass<out Enum<*>>,
    val message: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)


class EnumPatternValidator
    : ConstraintValidator<EnumPattern, CharSequence> {
    private val acceptedValues: MutableList<String> = mutableListOf()

    override fun initialize(constraintAnnotation: EnumPattern) {
        super.initialize(constraintAnnotation)
        
        acceptedValues.addAll(constraintAnnotation.enumClass.java
            .enumConstants
            .map { it.name }
        )
    }

    override fun isValid(value: CharSequence?, context: ConstraintValidatorContext): Boolean {
        return if (value == null) {
            true
        } else acceptedValues.contains(value.toString())
    }
    
}