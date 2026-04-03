package br.com.maikonspo.consumo.infra.entrypoint.user

data class UserResponseDto(
    val id: Long?,
    val name: String,
    val email: String
)