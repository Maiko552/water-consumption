package br.com.maikonspo.consumo.infra.gateway.expense

import br.com.maikonspo.consumo.core.entity.WaterExpense
import br.com.maikonspo.consumo.core.port.FindWaterExpenseByUserAndReferenceDatePort
import br.com.maikonspo.consumo.core.port.FindWaterExpensesByUserPort
import br.com.maikonspo.consumo.core.port.SaveWaterExpensePort
import org.springframework.stereotype.Component

@Component
class WaterExpenseGateway(
    private val repository: WaterExpenseRepository
) : SaveWaterExpensePort,
    FindWaterExpenseByUserAndReferenceDatePort,
    FindWaterExpensesByUserPort {

    override fun save(expense: WaterExpense): WaterExpense {
        val entity = WaterExpenseConverter.toEntity(expense)
        val saved = repository.save(entity)
        return WaterExpenseConverter.toDomain(saved)
    }

    override fun findByUserIdAndReferenceDate(
        userId: Long,
        referenceDate: java.time.LocalDate
    ): WaterExpense? {
        val entity = repository.findByUserIdAndReferenceDate(userId, referenceDate)
        return if (entity != null) WaterExpenseConverter.toDomain(entity) else null
    }

    override fun findByUserId(userId: Long): List<WaterExpense> {
        return repository.findAllByUserIdOrderByReferenceDateDesc(userId)
            .map(WaterExpenseConverter::toDomain)
    }
}