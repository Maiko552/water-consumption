package br.com.maikonspo.consumo.core.port

import br.com.maikonspo.consumo.core.entity.WaterExpense

interface FindWaterExpenseByUserAndReferenceDatePort {
    fun findByUserIdAndReferenceDate(
        userId: Long,
        referenceDate: java.time.LocalDate
    ): WaterExpense?
}