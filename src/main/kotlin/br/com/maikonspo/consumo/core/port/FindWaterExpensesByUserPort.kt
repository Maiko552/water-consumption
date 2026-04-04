package br.com.maikonspo.consumo.core.port

import br.com.maikonspo.consumo.core.entity.WaterExpense

interface FindWaterExpensesByUserPort {
    fun findByUserId(userId: Long): List<WaterExpense>
}