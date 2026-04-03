package br.com.maikonspo.consumo.core.port

interface EncryptPasswordPort {
    fun encryptPassword(password: String): String
}