package br.com.maikonspo.consumo.infra.delivery

import br.com.maikonspo.consumo.core.entity.request.RegisterWaterExpenseRequest
import br.com.maikonspo.consumo.core.usecase.FindWaterExpensesByUserUseCase
import br.com.maikonspo.consumo.core.usecase.RegisterWaterExpenseUseCase
import br.com.maikonspo.consumo.infra.entrypoint.expense.RegisterWaterExpenseDto
import br.com.maikonspo.consumo.infra.entrypoint.expense.WaterExpenseResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/water-expenses")
class WaterExpenseController(
    private val registerWaterExpenseUseCase: RegisterWaterExpenseUseCase,
    private val findWaterExpensesByUserUseCase: FindWaterExpensesByUserUseCase
) {

    @PostMapping
    fun create(@RequestBody dto: RegisterWaterExpenseDto): WaterExpenseResponseDto {
        val request = RegisterWaterExpenseRequest(
            userId = dto.userId,
            month = dto.month,
            year = dto.year,
            amount = dto.amount,
            consumptionInCubicMeters = dto.consumptionInCubicMeters,
            note = dto.note
        )

        val expense = registerWaterExpenseUseCase.execute(request)

        return WaterExpenseResponseDto(
            id = expense.id,
            userId = expense.userId,
            month = expense.month,
            year = expense.year,
            amount = expense.amount,
            consumptionInCubicMeters = expense.consumptionInCubicMeters,
            note = expense.note,
            createdDate = expense.createdDate
        )
    }

    @GetMapping("/user/{userId}")
    fun findByUser(@PathVariable userId: Long): List<WaterExpenseResponseDto> {
        return findWaterExpensesByUserUseCase.execute(userId)
            .map { expense ->
                WaterExpenseResponseDto(
                    id = expense.id,
                    userId = expense.userId,
                    month = expense.month,
                    year = expense.year,
                    amount = expense.amount,
                    consumptionInCubicMeters = expense.consumptionInCubicMeters,
                    note = expense.note,
                    createdDate = expense.createdDate
                )
            }
    }
}