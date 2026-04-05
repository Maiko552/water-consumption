package br.com.maikonspo.consumo.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig(
    @Value("\${security.jwt.secret}")
    val secret: String,

    @Value("\${security.jwt.expiration}")
    val expiration: Long
)