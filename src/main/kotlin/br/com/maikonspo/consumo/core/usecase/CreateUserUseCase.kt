package br.com.maikonspo.consumo.core.usecase

import br.com.maikonspo.consumo.core.entity.request.CreateUserRequest
import br.com.maikonspo.consumo.core.entity.User

interface CreateUserUseCase {
    fun execute(request: CreateUserRequest): User

}