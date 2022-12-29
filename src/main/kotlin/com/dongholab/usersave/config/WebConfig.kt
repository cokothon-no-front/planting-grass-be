package com.dongholab.usersave.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.util.MimeTypeUtils
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.ViewResolverRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ITemplateResolver

@Configuration
class WebConfig : WebFluxConfigurer, ApplicationContextAware {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    lateinit var context: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.context = applicationContext;
    }

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().apply {
            jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, MimeTypeUtils.APPLICATION_JSON))
            jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, MimeTypeUtils.APPLICATION_JSON))
        }
    }

    @Bean
    fun thymeleafTemplateResolver(): ITemplateResolver = SpringResourceTemplateResolver().apply {
        context?.let { setApplicationContext(it) }
        prefix = "classpath:templates/"
        suffix = ".html"
        templateMode = TemplateMode.HTML
        isCacheable = false
        checkExistence = false
    }

    @Bean
    fun thymeleafTemplateEngine(): ISpringWebFluxTemplateEngine = SpringWebFluxTemplateEngine().apply {
        setTemplateResolver(thymeleafTemplateResolver())
    }

    @Bean
    fun thymeleafReactiveViewResolver(): ThymeleafReactiveViewResolver = ThymeleafReactiveViewResolver().apply {
        templateEngine = thymeleafTemplateEngine()
    }

    override fun configureViewResolvers(registry: ViewResolverRegistry) {
        registry.viewResolver(thymeleafReactiveViewResolver())
    }

    override fun configureArgumentResolvers(configurer: ArgumentResolverConfigurer) {
        configurer.addCustomResolver(ReactivePageableHandlerMethodArgumentResolver())
    }

    override fun addCorsMappings(corsRegistry: CorsRegistry) {
        corsRegistry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "OPTIONS", "PUT")
    }
}