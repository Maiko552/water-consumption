package br.com.maikonspo.consumo.core.entity

data class CreateUserRequest(
    val name : String,
    val email : String,
    val password: String
) {

}