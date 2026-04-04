package br.com.maikonspo.consumo.core.usecase.impl

import br.com.maikonspo.consumo.core.entity.request.CreateUserRequest
import br.com.maikonspo.consumo.core.entity.User
import br.com.maikonspo.consumo.core.exception.UserAlreadyExistsException
import br.com.maikonspo.consumo.core.port.EncryptPasswordPort
import br.com.maikonspo.consumo.core.port.FindUserByEmailPort
import br.com.maikonspo.consumo.core.port.SaveUserPort
import br.com.maikonspo.consumo.core.usecase.CreateUserUseCase
import org.springframework.stereotype.Service

@Service
class CreateUserUseCaseImpl(
    private val saveUserPort: SaveUserPort,
    private val findUserByEmailPort: FindUserByEmailPort,
    private val encryptPassword: EncryptPasswordPort
) : CreateUserUseCase {

    override fun execute(request: CreateUserRequest): User {
        val existingUser = findUserByEmailPort.findByEmail(request.email)
        if (existingUser != null) {throw UserAlreadyExistsException()}

        val encrypt = encryptPassword.encryptPassword(request.password)
        val user = User(
            id = null,
            name = request.name,
            email = request.email,
            password = encrypt
        )

        return saveUserPort.save(user)
    }

}