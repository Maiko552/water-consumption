package br.com.maikonspo.consumo.core.usecase.impl

import br.com.maikonspo.consumo.core.entity.WaterExpense
import br.com.maikonspo.consumo.core.entity.request.RegisterWaterExpenseRequest
import br.com.maikonspo.consumo.core.exception.WaterExpenseAlreadyExistsException
import br.com.maikonspo.consumo.core.port.FindWaterExpenseByUserAndMonthPort
import br.com.maikonspo.consumo.core.port.SaveWaterExpensePort
import br.com.maikonspo.consumo.core.usecase.RegisterWaterExpenseUseCase
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RegisterWaterExpenseUseCaseImpl(
    private val saveWaterExpensePort: SaveWaterExpensePort,
    private val findWaterExpenseByUserAndMonthPort: FindWaterExpenseByUserAndMonthPort
) : RegisterWaterExpenseUseCase {

    override fun execute(request: RegisterWaterExpenseRequest): WaterExpense {
        val existingExpense = findWaterExpenseByUserAndMonthPort.findByUserIdAndMonth(
            userId = request.userId,
            month = request.month,
            year = request.year
        )

        if (existingExpense != null) {
            throw WaterExpenseAlreadyExistsException()
        }

        val expense = WaterExpense(
            id = null,
            userId = request.userId,
            month = request.month,
            year = request.year,
            amount = request.amount,
            consumptionInCubicMeters = request.consumptionInCubicMeters,
            note = request.note,
            createdDate = LocalDateTime.now()
        )

        return saveWaterExpensePort.save(expense)
    }
}