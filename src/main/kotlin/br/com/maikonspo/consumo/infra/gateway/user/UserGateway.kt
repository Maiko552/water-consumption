package br.com.maikonspo.consumo.infra.gateway.user

import br.com.maikonspo.consumo.core.entity.User
import br.com.maikonspo.consumo.core.port.FindUserByEmailPort
import br.com.maikonspo.consumo.core.port.SaveUserPort
import org.springframework.stereotype.Component

@Component
class UserGateway(
    private val userRepository: UserRepository
) : SaveUserPort, FindUserByEmailPort {

    override fun save(user: User): User {
        val entity = UserConverter.toEntity(user)
        val savedEntity = userRepository.save(entity)
        return UserConverter.toDomain(savedEntity)
    }

    override fun findByEmail(email: String): User? {
        val entity = userRepository.findByEmail(email)
        return entity?.let(UserConverter::toDomain)
    }
}