package br.com.maikonspo.consumo.infra.entrypoint.user

data class CreateUserDto(
    val name: String,
    val email: String,
    val password: String
)