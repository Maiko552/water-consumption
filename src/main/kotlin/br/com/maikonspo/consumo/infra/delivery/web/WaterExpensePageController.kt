package br.com.maikonspo.consumo.infra.delivery.web

import br.com.maikonspo.consumo.core.usecase.FindWaterExpensesByUserUseCase
import br.com.maikonspo.consumo.infra.security.UserDetailsImpl
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WaterExpensePageController(
    private val findWaterExpensesByUserUseCase: FindWaterExpensesByUserUseCase
) {

    @GetMapping("/water-expenses")
    fun listPage(model: Model): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication!!.principal as UserDetailsImpl
        val userId = userDetails.getUser().id!!

        val expenses = findWaterExpensesByUserUseCase.execute(userId)

        model.addAttribute("expenses", expenses)
        return "water-expense-list"
    }

    @GetMapping("/water-expenses/form")
    fun formPage(model: Model): String {
        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication!!.principal as UserDetailsImpl
        val userId = userDetails.getUser().id!!

        model.addAttribute("userId", userId)
        return "water-expense-form"
    }
}