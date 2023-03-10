package com.nofront.plantinggrassbe.config;

import com.nofront.plantinggrassbe.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationProvider jwtProvider;
    @Autowired
    private NaverAuthenticationProvider naverProvider;
    @Autowired
    private KakaoAuthenticationProvider kakaoProvider;
    @Autowired
    private GoogleAuthenticationProvider googleProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .authenticationProvider(jwtProvider)
            .authenticationProvider(naverProvider)
            .authenticationProvider(googleProvider)
            .authenticationProvider(kakaoProvider);
    }
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public SnsAuthenticationFilter snsFilter() throws Exception {
        return new SnsAuthenticationFilter(authenticationManagerBean());
    }
    @Bean
    public JwtAuthenticationFilter jwtFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManagerBean());
    }

    @Bean
    @Primary
    public FilterRegistrationBean<SnsAuthenticationFilter> snsRegistrationFilter(SnsAuthenticationFilter filter) throws Exception {
        FilterRegistrationBean<SnsAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtRegistrationFilter(JwtAuthenticationFilter filter) throws Exception {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CORS ????????????
        // CSRF ????????????
        // JSESSION ????????????
        // ?????? ????????? ????????? ?????? ????????? ?????? ?????? ??????
        // UsernamePasswordAuthenticationFilter ??? ?????? Jwt Handler Filter
        http
            .cors().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/refresh").permitAll()
                .antMatchers(HttpMethod.GET, "/oauth2/kakao").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(snsFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
            ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/refresh")
                .antMatchers("/oauth2/kakao")
                .antMatchers("/user/sign-up")
                .antMatchers("/user/sign-in")
        ;
    }
}
