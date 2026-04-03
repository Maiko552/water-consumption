package br.com.maikonspo.consumo.core.port

import br.com.maikonspo.consumo.core.entity.User

interface SaveUserPort {
    fun save(user: User): User
}