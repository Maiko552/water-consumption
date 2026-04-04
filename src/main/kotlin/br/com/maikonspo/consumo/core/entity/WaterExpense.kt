package br.com.maikonspo.consumo.core.entity

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class WaterExpense(
    val id: Long?,
    val userId: Long,
    val referenceDate: LocalDate,
    val dueDate: LocalDate,
    val totalAmount: BigDecimal,
    val consumptionM3: Double?,
    val waterAmount: BigDecimal?,
    val sewageAmount: BigDecimal?,
    val meterReading: Double?,
    val isPaid: Boolean,
    val note: String?,
    val createdDate: LocalDateTime
)