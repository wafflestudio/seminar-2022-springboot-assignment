package com.wafflestudio.seminar.global

import org.hibernate.EmptyInterceptor
import org.hibernate.cfg.AvailableSettings
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import javax.sql.DataSource
import org.springframework.orm.hibernate5.SpringBeanContainer


interface HibernateQueryCounter {
    fun <K> count(block: () -> K) : Result<K>
    
    data class Result<K>(
        val value:K,
        val queryCount: Int,
    )
}

class HibernateQueryCounterImpl : EmptyInterceptor(), HibernateQueryCounter {
    private val isCCounting: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }
    private val queryCount: ThreadLocal<Int> = ThreadLocal.withInitial { 0 }

    override fun onPrepareStatement(sql: String?): String {
        if (isCCounting.get()) {
            queryCount.set(queryCount.get() + 1)
        }
        return super.onPrepareStatement(sql);
    }

    override fun <K> count(block: () -> K): HibernateQueryCounter.Result<K> {
        isCCounting.set(true)
        queryCount.set(0)
        
        val result = block()
        
        isCCounting.set(false)
        return HibernateQueryCounter.Result(result, queryCount.get())
    }
}

@Configuration
class HibernateConfig {
    @Bean
    fun entityManagerFactory(
        factory: EntityManagerFactoryBuilder,
        dataSource: DataSource,
        jpaProperties: JpaProperties,
        hibernateProperties: HibernateProperties,
        beanFactory: ConfigurableListableBeanFactory,
        customInterceptor: HibernateQueryCounterImpl,
    ): LocalContainerEntityManagerFactoryBean? {
        val properties: Map<String, Any> =
            hibernateProperties.determineHibernateProperties(jpaProperties.properties, HibernateSettings())
                .also { it["hibernate.ejb.interceptor"] = customInterceptor }

        return factory.dataSource(dataSource)
            .packages("com.wafflestudio.seminar")
            .properties(properties)
            .build()
            .also { it.jpaPropertyMap[AvailableSettings.BEAN_CONTAINER] = SpringBeanContainer(beanFactory) }
    }

    @Bean
    fun customInterceptor() = HibernateQueryCounterImpl()
}
