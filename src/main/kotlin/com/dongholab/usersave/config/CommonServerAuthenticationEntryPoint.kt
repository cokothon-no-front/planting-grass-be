package com.dongholab.usersave.config

import com.dongholab.usersave.constants.ExceptionConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.dongholab.usersave.domain.response.ResponseMessage
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.Charset

@Component
class CommonServerAuthenticationEntryPoint(val objectMapper: ObjectMapper): ServerAuthenticationEntryPoint {
    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException): Mono<Void> {
        return Mono.defer { Mono.just(exchange.response) }
            .flatMap { response ->
                response.run {
                    statusCode = ExceptionConstants.unauthorizedException.statusCode
                    headers.contentType = MediaType.APPLICATION_JSON

                    val dataBufferFactory: DataBufferFactory = bufferFactory()
                    val message = ResponseMessage(ExceptionConstants.unauthorizedException.statusText)
                    val result: String = objectMapper.writeValueAsString(message)
                    val buffer: DataBuffer = dataBufferFactory.wrap(
                        result.toByteArray(
                            Charset.defaultCharset()
                        )
                    )
                    writeWith(Mono.just(buffer))
                }
            }
    }
}