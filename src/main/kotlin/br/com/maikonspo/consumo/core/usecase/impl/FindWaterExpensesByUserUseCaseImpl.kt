package br.com.maikonspo.consumo.core.usecase.impl

import br.com.maikonspo.consumo.core.entity.WaterExpense
import br.com.maikonspo.consumo.core.port.FindWaterExpensesByUserPort
import br.com.maikonspo.consumo.core.usecase.FindWaterExpensesByUserUseCase
import org.springframework.stereotype.Service

@Service
class FindWaterExpensesByUserUseCaseImpl(
    private val findWaterExpensesByUserPort: FindWaterExpensesByUserPort
) : FindWaterExpensesByUserUseCase {

    override fun execute(userId: Long): List<WaterExpense> {
        return findWaterExpensesByUserPort.findByUserId(userId)
    }
}