package com.dongholab.usersave.constants

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException

object ExceptionConstants {
    val unauthorizedException: HttpServerErrorException = HttpServerErrorException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다")
}