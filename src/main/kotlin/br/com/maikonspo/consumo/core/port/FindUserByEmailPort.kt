package br.com.maikonspo.consumo.core.port

import br.com.maikonspo.consumo.core.entity.User

interface FindUserByEmailPort {
    fun findByEmail(email: String): User?
}