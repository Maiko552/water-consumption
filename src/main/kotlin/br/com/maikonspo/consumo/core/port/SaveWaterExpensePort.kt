package br.com.maikonspo.consumo.core.port

import br.com.maikonspo.consumo.core.entity.WaterExpense

interface SaveWaterExpensePort {
    fun save(expense: WaterExpense): WaterExpense
}