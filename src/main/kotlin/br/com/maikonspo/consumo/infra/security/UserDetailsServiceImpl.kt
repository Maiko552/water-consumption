package br.com.maikonspo.consumo.infra.security

import br.com.maikonspo.consumo.core.port.FindUserByEmailPort
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val findUserByEmailPort: FindUserByEmailPort
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
        println("Tentando autenticar usuário com email: $email")

        val user = findUserByEmailPort.findByEmail(email)
            ?: throw UsernameNotFoundException("Usuário não encontrado")

        println("Usuário encontrado: ${user.email}")

        return UserDetailsImpl(user)
    }
}