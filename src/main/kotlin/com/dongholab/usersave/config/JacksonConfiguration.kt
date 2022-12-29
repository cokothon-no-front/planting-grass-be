package com.dongholab.usersave.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class JacksonConfiguration {

    @Bean("objectMapper")
    fun objectMapper(): ObjectMapper = Jackson2ObjectMapperBuilder().build<ObjectMapper>().apply {
        val kotlinModule: KotlinModule = KotlinModule.Builder()
            .configure(KotlinFeature.StrictNullChecks, true)
            .build()
        registerModule(kotlinModule)
        registerModule(ParameterNamesModule())
        registerModule(JavaTimeModule())
        registerModule(Jdk8Module())
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        setSerializationInclusion(JsonInclude.Include.ALWAYS)
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }
}
