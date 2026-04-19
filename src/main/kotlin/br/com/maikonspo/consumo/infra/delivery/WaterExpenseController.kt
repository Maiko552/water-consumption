package br.com.maikonspo.consumo.infra.entrypoint.expense


import br.com.maikonspo.consumo.core.entity.request.RegisterWaterExpenseRequest
import br.com.maikonspo.consumo.core.usecase.FindWaterExpensesByUserUseCase
import br.com.maikonspo.consumo.core.usecase.RegisterWaterExpenseUseCase
import br.com.maikonspo.consumo.infra.security.UserDetailsImpl
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/water-expenses")
class WaterExpenseController(
    private val registerWaterExpenseUseCase: RegisterWaterExpenseUseCase,
    private val findWaterExpensesByUserUseCase: FindWaterExpensesByUserUseCase
) {

    @PostMapping
    fun create(
        @RequestBody dto: RegisterWaterExpenseDto,
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): WaterExpenseResponseDto {
        val request = RegisterWaterExpenseRequest(
            userId = userDetails.getUser().id!!,
            referenceDate = dto.referenceDate,
            dueDate = dto.dueDate,
            totalAmount = dto.totalAmount,
            consumptionM3 = dto.consumptionM3,
            waterAmount = dto.waterAmount,
            sewageAmount = dto.sewageAmount,
            meterReading = dto.meterReading,
            isPaid = dto.isPaid,
            note = dto.note
        )

        val expense = registerWaterExpenseUseCase.execute(request)

        return WaterExpenseResponseDto(
            id = expense.id,
            referenceDate = expense.referenceDate,
            dueDate = expense.dueDate,
            totalAmount = expense.totalAmount,
            consumptionM3 = expense.consumptionM3,
            waterAmount = expense.waterAmount,
            sewageAmount = expense.sewageAmount,
            meterReading = expense.meterReading,
            isPaid = expense.isPaid,
            note = expense.note,
            createdDate = expense.createdDate
        )
    }

    @GetMapping
    fun findByAuthenticatedUser(
        @AuthenticationPrincipal userDetails: UserDetailsImpl
    ): List<WaterExpenseResponseDto> {
        return findWaterExpensesByUserUseCase.execute(userDetails.getUser().id!!)
            .map { expense ->
                WaterExpenseResponseDto(
                    id = expense.id,
                    referenceDate = expense.referenceDate,
                    dueDate = expense.dueDate,
                    totalAmount = expense.totalAmount,
                    consumptionM3 = expense.consumptionM3,
                    waterAmount = expense.waterAmount,
                    sewageAmount = expense.sewageAmount,
                    meterReading = expense.meterReading,
                    isPaid = expense.isPaid,
                    note = expense.note,
                    createdDate = expense.createdDate
                )
            }
    }
}
