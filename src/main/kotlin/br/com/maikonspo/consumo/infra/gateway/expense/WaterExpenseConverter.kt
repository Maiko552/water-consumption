package br.com.maikonspo.consumo.infra.gateway.expense

import br.com.maikonspo.consumo.core.entity.WaterExpense

object WaterExpenseConverter {

    fun toEntity(domain: WaterExpense): WaterExpenseEntity {
        return WaterExpenseEntity(
            id = domain.id,
            userId = domain.userId,
            month = domain.month,
            year = domain.year,
            amount = domain.amount,
            consumptionInCubicMeters = domain.consumptionInCubicMeters,
            note = domain.note,
            createdDate = domain.createdDate
        )
    }

    fun toDomain(entity: WaterExpenseEntity): WaterExpense {
        return WaterExpense(
            id = entity.id,
            userId = entity.userId,
            month = entity.month,
            year = entity.year,
            amount = entity.amount,
            consumptionInCubicMeters = entity.consumptionInCubicMeters,
            note = entity.note,
            createdDate = entity.createdDate
        )
    }
}