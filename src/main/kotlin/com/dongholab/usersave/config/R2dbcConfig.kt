package com.dongholab.usersave.config

import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory
import io.netty.util.CharsetUtil
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import java.net.URLDecoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import com.github.jasync.sql.db.Configuration as JasyncConfiguration

@Configuration
@EnableR2dbcAuditing
class R2dbcConfig: AbstractR2dbcConfiguration() {
    companion object {
        val r2dbcRegex = Regex("r2dbc:(.*):\\/\\/(.*):([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])\\/(\\w+)\\??(.*)")
        val r2dbcPrefix = "spring.r2dbc"
    }

    @Autowired
    private val env: Environment? = null

    fun r2dbcConnectorParser(r2dbcUrl: String, userName: String = "root", password: String = "root"): JasyncConfiguration? {
        val result: MatchResult? = r2dbcRegex.find(r2dbcUrl)
        return result?.let {
            val groupValues = it.groupValues
            (groupValues.size > 5).let {
                val connector = groupValues.get(1)
                val host = groupValues.get(2)
                val port: Int = groupValues.getOrNull(3)?.toInt() ?: 3306
                val database = groupValues.get(4)
                val queryString: String? = groupValues.getOrNull(5)

                if (!listOf("mysql", "mariadb").contains(connector)) {
                    throw Exception("connector type error")
                }

                val queryMap: Map<String, String>? = queryString?.isNotBlank()?.let {
                    queryString.split("&")?.mapNotNull {
                        val keyValue = it.split("=")
                        keyValue.run {
                            if (size == 2) {
                                val key = get(0)
                                val value = URLDecoder.decode(get(1), StandardCharsets.UTF_8.toString())
                                key to value
                            } else null
                        }
                    }.toMap()
                }

               JasyncConfiguration(
                    host = host,
                    port = port,
                    database = database,
                    charset = queryMap?.get("characterEncoding")?.let {
                        if (it == "utf8") {
                            CharsetUtil.UTF_8
                        } else {
                            Charset.forName(it)
                        }
                    } ?: CharsetUtil.UTF_8,
                    username = userName,
                    password = password
                )
            }
        }
    }

    override fun connectionFactory(): ConnectionFactory {
        val r2dbcUrl: String? = env?.getProperty("$r2dbcPrefix.url")
        val userName = env?.getProperty("$r2dbcPrefix.username")
        val password = env?.getProperty("$r2dbcPrefix.password")

        val connector = r2dbcUrl?.let {
            if (userName != null && password != null) {
                r2dbcConnectorParser(
                    r2dbcUrl = it,
                    userName = userName,
                    password = password
                )
            } else {
                if (userName != null) {
                    r2dbcConnectorParser(
                        r2dbcUrl = it,
                        userName = userName
                    )
                } else if (password != null) {
                    r2dbcConnectorParser(
                        r2dbcUrl = it,
                        password = password
                    )
                } else {
                    r2dbcConnectorParser(
                        r2dbcUrl = it
                    )
                }
            }
        } ?: throw Exception()

        return connector?.let {
            JasyncConnectionFactory(MySQLConnectionFactory(
                it
            ))
        }
    }
}