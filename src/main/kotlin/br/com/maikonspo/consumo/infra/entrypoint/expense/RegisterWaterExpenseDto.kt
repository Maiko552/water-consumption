package br.com.maikonspo.consumo.infra.entrypoint.expense

import java.math.BigDecimal

data class RegisterWaterExpenseDto(
    val userId: Long,
    val month: Int,
    val year: Int,
    val amount: BigDecimal,
    val consumptionInCubicMeters: Double?,
    val note: String?
)