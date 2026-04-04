package br.com.maikonspo.consumo.core.usecase

import br.com.maikonspo.consumo.core.entity.WaterExpense

interface FindWaterExpensesByUserUseCase {
    fun execute(userId: Long): List<WaterExpense>
}