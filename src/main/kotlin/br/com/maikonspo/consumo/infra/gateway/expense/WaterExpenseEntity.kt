package br.com.maikonspo.consumo.infra.gateway.expense

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "water_expense")
class WaterExpenseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "reference_date", nullable = false)
    var referenceDate: LocalDate,

    @Column(name = "due_date", nullable = false)
    var dueDate: LocalDate,

    @Column(name = "total_amount", nullable = false)
    var totalAmount: BigDecimal,

    @Column(name = "consumption_m3")
    var consumptionM3: Double?,

    @Column(name = "water_amount")
    var waterAmount: BigDecimal?,

    @Column(name = "sewage_amount")
    var sewageAmount: BigDecimal?,

    @Column(name = "meter_reading")
    var meterReading: Double?,

    @Column(name = "is_paid", nullable = false)
    var isPaid: Boolean,

    @Column
    var note: String?,

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime
)