package br.com.maikonspo.consumo.infra.gateway.expense

import org.springframework.data.jpa.repository.JpaRepository

interface WaterExpenseRepository : JpaRepository<WaterExpenseEntity, Long> {

    fun findByUserIdAndMonthAndYear(
        userId: Long,
        month: Int,
        year: Int
    ): WaterExpenseEntity?

    fun findAllByUserIdOrderByYearDescMonthDesc(userId: Long): List<WaterExpenseEntity>
}