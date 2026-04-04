package br.com.maikonspo.consumo.core.usecase

import br.com.maikonspo.consumo.core.entity.WaterExpense
import br.com.maikonspo.consumo.core.entity.request.RegisterWaterExpenseRequest

interface RegisterWaterExpenseUseCase {
    fun execute(request: RegisterWaterExpenseRequest): WaterExpense
}