package br.com.maikonspo.consumo.infra.delivery

import br.com.maikonspo.consumo.core.entity.request.CreateUserRequest
import br.com.maikonspo.consumo.core.usecase.CreateUserUseCase
import br.com.maikonspo.consumo.infra.entrypoint.user.CreateUserDto
import br.com.maikonspo.consumo.infra.entrypoint.user.UserResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
    private val createUserUseCase: CreateUserUseCase
) {

    @PostMapping
    fun create(@RequestBody dto: CreateUserDto): UserResponseDto {
        val request = CreateUserRequest(
            name = dto.name,
            email = dto.email,
            password = dto.password
        )

        val user = createUserUseCase.execute(request)

        return UserResponseDto(
            id = user.id,
            name = user.name,
            email = user.email
        )
    }
}