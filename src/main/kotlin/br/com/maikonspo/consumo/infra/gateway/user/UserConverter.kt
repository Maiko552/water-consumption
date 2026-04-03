package br.com.maikonspo.consumo.infra.gateway.user

import br.com.maikonspo.consumo.core.entity.User

object UserConverter {

    fun toEntity(user: User): UserEntity {
        return UserEntity(
            id = user.id,
            name = user.name,
            email = user.email,
            password = user.password
        )
    }

    fun toDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            name = entity.name,
            email = entity.email,
            password = entity.password
        )
    }
}