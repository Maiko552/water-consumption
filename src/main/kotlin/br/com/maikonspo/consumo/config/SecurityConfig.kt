package br.com.maikonspo.consumo.config

import br.com.maikonspo.consumo.infra.security.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
    private val userDetailsServiceImpl: UserDetailsServiceImpl,
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/login", "/css/**", "/js/**").permitAll()
                it.requestMatchers(HttpMethod.POST, "/user").permitAll()
                it.anyRequest().authenticated()
            }
            .authenticationProvider(authenticationProvider())
            .formLogin {
                it.loginPage("/login")
                it.loginProcessingUrl("/login")
                it.defaultSuccessUrl("/water-expenses", true)
                it.failureUrl("/login?error=true")
                it.permitAll()
            }
            .logout {
                it.logoutSuccessUrl("/login")
                it.permitAll()
            }

        return http.build()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider(userDetailsServiceImpl)
        provider.setPasswordEncoder(passwordEncoder)
        return provider
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}