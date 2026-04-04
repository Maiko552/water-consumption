package br.com.maikonspo.consumo.infra.gateway.security

import br.com.maikonspo.consumo.infra.gateway.user.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userEntity = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Usuário não encontrado")

        return User.builder()
            .username(userEntity.email)
            .password(userEntity.password)
            .authorities(SimpleGrantedAuthority("ROLE_USER"))
            .build()
    }
}