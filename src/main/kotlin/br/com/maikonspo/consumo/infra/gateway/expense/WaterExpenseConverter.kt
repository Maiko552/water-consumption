package br.com.maikonspo.consumo.infra.gateway.expense

import br.com.maikonspo.consumo.core.entity.WaterExpense

object WaterExpenseConverter {

    fun toEntity(domain: WaterExpense): WaterExpenseEntity {
        return WaterExpenseEntity(
            id = domain.id,
            userId = domain.userId,
            referenceDate = domain.referenceDate,
            dueDate = domain.dueDate,
            totalAmount = domain.totalAmount,
            consumptionM3 = domain.consumptionM3,
            waterAmount = domain.waterAmount,
            sewageAmount = domain.sewageAmount,
            meterReading = domain.meterReading,
            isPaid = domain.isPaid,
            note = domain.note,
            createdDate = domain.createdDate
        )
    }

    fun toDomain(entity: WaterExpenseEntity): WaterExpense {
        return WaterExpense(
            id = entity.id,
            userId = entity.userId,
            referenceDate = entity.referenceDate,
            dueDate = entity.dueDate,
            totalAmount = entity.totalAmount,
            consumptionM3 = entity.consumptionM3,
            waterAmount = entity.waterAmount,
            sewageAmount = entity.sewageAmount,
            meterReading = entity.meterReading,
            isPaid = entity.isPaid,
            note = entity.note,
            createdDate = entity.createdDate
        )
    }
}