package br.com.maikonspo.consumo.infra.delivery.web


import br.com.maikonspo.consumo.core.entity.request.RegisterWaterExpenseRequest
import br.com.maikonspo.consumo.core.usecase.RegisterWaterExpenseUseCase
import br.com.maikonspo.consumo.infra.security.UserDetailsImpl
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PostMapping
import java.math.BigDecimal
import java.time.LocalDate

@Controller
class WaterExpenseWebController(
    private val registerWaterExpenseUseCase: RegisterWaterExpenseUseCase
) {

    @PostMapping("/water-expenses/save")
    fun saveWaterExpense(
        referenceDate: LocalDate,
        dueDate: LocalDate,
        totalAmount: BigDecimal,
        consumptionM3: Double?,
        waterAmount: BigDecimal?,
        sewageAmount: BigDecimal?,
        meterReading: Double?,
        isPaid: Boolean,
        note: String?,
        model: Model,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): String {
        val userId = userDetails.getUser().id!!

        return try {
            val request = RegisterWaterExpenseRequest(
                userId = userId,
                referenceDate = referenceDate,
                dueDate = dueDate,
                totalAmount = totalAmount,
                consumptionM3 = consumptionM3,
                waterAmount = waterAmount,
                sewageAmount = sewageAmount,
                meterReading = meterReading,
                isPaid = isPaid,
                note = note
            )

            registerWaterExpenseUseCase.execute(request)
            model.addAttribute("success", "Conta salva com sucesso!")
            "water-expense-form"
        } catch (ex: Exception) {
            model.addAttribute("error", ex.message)
            "water-expense-form"
        }
    }
}
