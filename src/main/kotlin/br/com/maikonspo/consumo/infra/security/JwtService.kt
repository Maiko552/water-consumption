package br.com.maikonspo.consumo.infra.security

import br.com.maikonspo.consumo.config.JwtConfig
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    private val jwtConfig: JwtConfig
) {

    private fun getSignKey(): SecretKey {
        return Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray())
    }

    fun generateToken(username: String): String {
        val now = Date()
        val expirationDate = Date(now.time + jwtConfig.expiration)

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(getSignKey())
            .compact()
    }

    fun extractUsername(token: String): String {
        return extractAllClaims(token).subject
    }

    fun isTokenValid(token: String, username: String): Boolean {
        val extractedUsername = extractUsername(token)
        return extractedUsername == username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractAllClaims(token).expiration.before(Date())
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSignKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }
}