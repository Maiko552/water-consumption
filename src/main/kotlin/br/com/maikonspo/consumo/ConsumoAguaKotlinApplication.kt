package br.com.maikonspo.consumo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ConsumoAguaKotlinApplication

fun main(args: Array<String>) {
	runApplication<ConsumoAguaKotlinApplication>(*args)
}
