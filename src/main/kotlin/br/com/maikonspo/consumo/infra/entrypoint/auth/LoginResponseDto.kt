package br.com.maikonspo.consumo.infra.entrypoint.auth

data class LoginResponseDto(
    val token: String,
    val type: String = "Bearer"
)