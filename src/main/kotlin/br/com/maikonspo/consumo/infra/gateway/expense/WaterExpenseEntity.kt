package br.com.maikonspo.consumo.infra.gateway.expense

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "water_expense")
class WaterExpenseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(nullable = false)
    var month: Int,

    @Column(nullable = false)
    var year: Int,

    @Column(nullable = false)
    var amount: BigDecimal,

    @Column(name = "consumption_m3")
    var consumptionInCubicMeters: Double?,

    @Column
    var note: String?,

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime
)