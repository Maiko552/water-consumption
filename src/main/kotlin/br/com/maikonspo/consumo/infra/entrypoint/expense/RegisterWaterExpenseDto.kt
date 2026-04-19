package br.com.maikonspo.consumo.infra.entrypoint.expense

import java.math.BigDecimal
import java.time.LocalDate

data class RegisterWaterExpenseDto(
    val referenceDate: LocalDate,
    val dueDate: LocalDate,
    val totalAmount: BigDecimal,
    val consumptionM3: Double?,
    val waterAmount: BigDecimal?,
    val sewageAmount: BigDecimal?,
    val meterReading: Double?,
    val isPaid: Boolean,
    val note: String?
)
