package br.com.maikonspo.consumo.core.usecase.impl

import br.com.maikonspo.consumo.core.entity.WaterExpense
import br.com.maikonspo.consumo.core.entity.request.RegisterWaterExpenseRequest
import br.com.maikonspo.consumo.core.exception.WaterExpenseAlreadyExistsException
import br.com.maikonspo.consumo.core.port.FindWaterExpenseByUserAndReferenceDatePort
import br.com.maikonspo.consumo.core.port.SaveWaterExpensePort
import br.com.maikonspo.consumo.core.usecase.RegisterWaterExpenseUseCase
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RegisterWaterExpenseUseCaseImpl(
    private val saveWaterExpensePort: SaveWaterExpensePort,
    private val findWaterExpenseByUserAndReferenceDatePort: FindWaterExpenseByUserAndReferenceDatePort
) : RegisterWaterExpenseUseCase {

    override fun execute(request: RegisterWaterExpenseRequest): WaterExpense {
        val existingExpense = findWaterExpenseByUserAndReferenceDatePort.findByUserIdAndReferenceDate(
            userId = request.userId,
            referenceDate = request.referenceDate
        )

        if (existingExpense != null) {
            throw WaterExpenseAlreadyExistsException()
        }

        val expense = WaterExpense(
            id = null,
            userId = request.userId,
            referenceDate = request.referenceDate,
            dueDate = request.dueDate,
            totalAmount = request.totalAmount,
            consumptionM3 = request.consumptionM3,
            waterAmount = request.waterAmount,
            sewageAmount = request.sewageAmount,
            meterReading = request.meterReading,
            isPaid = request.isPaid,
            note = request.note,
            createdDate = LocalDateTime.now()
        )

        return saveWaterExpensePort.save(expense)
    }
}