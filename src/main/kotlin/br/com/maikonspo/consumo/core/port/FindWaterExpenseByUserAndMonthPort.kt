package br.com.maikonspo.consumo.core.port

import br.com.maikonspo.consumo.core.entity.WaterExpense

interface FindWaterExpenseByUserAndMonthPort {
    fun findByUserIdAndMonth(
        userId: Long,
        month: Int,
        year: Int
    ): WaterExpense?
}