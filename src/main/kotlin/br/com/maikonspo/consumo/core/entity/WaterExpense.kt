package br.com.maikonspo.consumo.core.entity

import java.math.BigDecimal
import java.time.LocalDateTime

data class WaterExpense(
    val id: Long?,
    val userId: Long,
    val month: Int,
    val year: Int,
    val amount: BigDecimal,
    val consumptionInCubicMeters: Double?,
    val note: String?,
    val createdDate: LocalDateTime
)
