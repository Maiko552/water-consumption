package br.com.maikonspo.consumo.core.entity.request

data class CreateUserRequest(
    val name : String,
    val email : String,
    val password: String
) {

}