package br.com.maikonspo.consumo.core.entity.request

import java.math.BigDecimal

data class RegisterWaterExpenseRequest(
    val userId: Long,
    val month: Int,
    val year: Int,
    val amount: BigDecimal,
    val consumptionInCubicMeters: Double?,
    val note: String?
)