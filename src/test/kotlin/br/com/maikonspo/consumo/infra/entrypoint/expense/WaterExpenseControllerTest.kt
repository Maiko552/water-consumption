package br.com.maikonspo.consumo.infra.entrypoint.expense

import br.com.maikonspo.consumo.core.entity.User
import br.com.maikonspo.consumo.core.entity.WaterExpense
import br.com.maikonspo.consumo.core.entity.request.RegisterWaterExpenseRequest
import br.com.maikonspo.consumo.core.usecase.FindWaterExpensesByUserUseCase
import br.com.maikonspo.consumo.core.usecase.RegisterWaterExpenseUseCase
import br.com.maikonspo.consumo.infra.security.UserDetailsImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class WaterExpenseControllerTest {

    private val registerWaterExpenseUseCase = FakeRegisterWaterExpenseUseCase()
    private val findWaterExpensesByUserUseCase = FakeFindWaterExpensesByUserUseCase()

    private val controller = WaterExpenseController(
        registerWaterExpenseUseCase = registerWaterExpenseUseCase,
        findWaterExpensesByUserUseCase = findWaterExpensesByUserUseCase
    )

    @Test
    fun `create should use authenticated user id when building request`() {
        val authenticatedUserId = 42L
        val dto = RegisterWaterExpenseDto(
            referenceDate = LocalDate.of(2026, 4, 1),
            dueDate = LocalDate.of(2026, 4, 10),
            totalAmount = BigDecimal("145.90"),
            consumptionM3 = 12.5,
            waterAmount = BigDecimal("80.00"),
            sewageAmount = BigDecimal("65.90"),
            meterReading = 235.7,
            isPaid = false,
            note = "Conta de abril"
        )

        registerWaterExpenseUseCase.expenseToReturn = waterExpense(userId = authenticatedUserId)

        controller.create(dto, authenticatedPrincipal(authenticatedUserId))

        assertEquals(authenticatedUserId, registerWaterExpenseUseCase.receivedRequest?.userId)
        assertEquals(dto.referenceDate, registerWaterExpenseUseCase.receivedRequest?.referenceDate)
        assertEquals(dto.dueDate, registerWaterExpenseUseCase.receivedRequest?.dueDate)
        assertEquals(dto.totalAmount, registerWaterExpenseUseCase.receivedRequest?.totalAmount)
    }

    @Test
    fun `findByAuthenticatedUser should query expenses using authenticated user id`() {
        val authenticatedUserId = 99L

        findWaterExpensesByUserUseCase.expensesToReturn = listOf(waterExpense(userId = authenticatedUserId))

        val response = controller.findByAuthenticatedUser(authenticatedPrincipal(authenticatedUserId))

        assertEquals(authenticatedUserId, findWaterExpensesByUserUseCase.receivedUserId)
        assertEquals(1, response.size)
        assertEquals(LocalDate.of(2026, 4, 1), response.first().referenceDate)
        assertEquals(BigDecimal("145.90"), response.first().totalAmount)
    }

    private fun authenticatedPrincipal(userId: Long): UserDetailsImpl {
        return UserDetailsImpl(
            User(
                id = userId,
                name = "Maria",
                email = "maria@email.com",
                password = "senha-criptografada"
            )
        )
    }

    private fun waterExpense(userId: Long): WaterExpense {
        return WaterExpense(
            id = 1L,
            userId = userId,
            referenceDate = LocalDate.of(2026, 4, 1),
            dueDate = LocalDate.of(2026, 4, 10),
            totalAmount = BigDecimal("145.90"),
            consumptionM3 = 12.5,
            waterAmount = BigDecimal("80.00"),
            sewageAmount = BigDecimal("65.90"),
            meterReading = 235.7,
            isPaid = false,
            note = "Conta de abril",
            createdDate = LocalDateTime.of(2026, 4, 1, 8, 0)
        )
    }

    private class FakeRegisterWaterExpenseUseCase : RegisterWaterExpenseUseCase {
        var receivedRequest: RegisterWaterExpenseRequest? = null
        lateinit var expenseToReturn: WaterExpense

        override fun execute(request: RegisterWaterExpenseRequest): WaterExpense {
            receivedRequest = request
            return expenseToReturn
        }
    }

    private class FakeFindWaterExpensesByUserUseCase : FindWaterExpensesByUserUseCase {
        var receivedUserId: Long? = null
        var expensesToReturn: List<WaterExpense> = emptyList()

        override fun execute(userId: Long): List<WaterExpense> {
            receivedUserId = userId
            return expensesToReturn
        }
    }
}
