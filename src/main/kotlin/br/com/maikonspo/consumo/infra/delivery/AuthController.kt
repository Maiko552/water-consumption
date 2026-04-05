package br.com.maikonspo.consumo.infra.delivery


import br.com.maikonspo.consumo.core.entity.request.LoginRequest
import br.com.maikonspo.consumo.infra.entrypoint.auth.LoginResponseDto
import br.com.maikonspo.consumo.infra.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): LoginResponseDto {

        val authentication = UsernamePasswordAuthenticationToken(
            request.email,
            request.password
        )

        authenticationManager.authenticate(authentication)

        val token = jwtService.generateToken(request.email)

        return LoginResponseDto(token = token)
    }
}