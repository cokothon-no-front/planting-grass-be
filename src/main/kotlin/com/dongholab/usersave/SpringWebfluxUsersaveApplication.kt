package com.dongholab.usersave

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = ["com.dongholab.usersave.repository"])
@ConfigurationPropertiesScan
class SpringWebfluxUsersaveApplication

fun main(args: Array<String>) {
    runApplication<SpringWebfluxUsersaveApplication>(*args)
}
