package br.com.maikonspo.consumo.infra.delivery.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PageController {

    @GetMapping("/login")
    fun loginPage(): String {
        return "login"
    }
}