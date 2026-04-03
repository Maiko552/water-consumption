package br.com.maikonspo.consumo.infra.gateway.security

import br.com.maikonspo.consumo.core.port.EncryptPasswordPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class EncryptPasswordGateway(
    private val passwordEncoder: PasswordEncoder
) : EncryptPasswordPort {

    override fun encryptPassword(password: String): String {
        return passwordEncoder.encode(password)!!
    }
}