package com.example.demo.config;

import com.example.demo.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Value("${demo.disable.security: false}")
    private boolean securityDisabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (!securityDisabled) {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/admin/**").hasAnyRole("ADMIN")
                    .antMatchers("/vk/auth/**").permitAll()
                    .antMatchers("/yandex/auth/**").permitAll()
                    .antMatchers("/oauth2/**").permitAll()
                    .antMatchers("/yan/**").permitAll()
                    .antMatchers("/save").permitAll()
                    .anyRequest().permitAll()
                    .and()
                    .formLogin()
                    .loginPage("/api/auth")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .and()
                    .headers().frameOptions().disable();
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        } else {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .anyRequest().permitAll()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                    .and()
                    .headers().frameOptions().disable();
        }
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
