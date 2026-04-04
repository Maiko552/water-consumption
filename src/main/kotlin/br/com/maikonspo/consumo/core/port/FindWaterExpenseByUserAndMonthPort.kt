package br.com.maikonspo.consumo.core.port

import br.com.maikonspo.consumo.core.entity.WaterExpense
import java.time.LocalDate

interface FindWaterExpenseByUserAndReferenceDatePort {
    fun findByUserIdAndReferenceDate(
        userId: Long,
        referenceDate: LocalDate
    ): WaterExpense?
}