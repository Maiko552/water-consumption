package br.com.maikonspo.consumo.infra.gateway.expense

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface WaterExpenseRepository : JpaRepository<WaterExpenseEntity, Long> {

    fun findByUserIdAndReferenceDate(
        userId: Long,
        referenceDate: LocalDate
    ): WaterExpenseEntity?

    fun findAllByUserIdOrderByReferenceDateDesc(userId: Long): List<WaterExpenseEntity>
}