package io.hhplus.tdd.point

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach

class UserPointTest {
    private val sut = UserPoint(
        id = 1L,
        point = 0,
        updateMillis = System.currentTimeMillis(),
    )

    @BeforeEach fun setup() {
        sut.point = 0
    }

    @Test
    fun `point 충전 시, 충전 최소 금액보다 작으면 실패`(){
        val amount = UserPoint.MIN_AMOUNT_PER_CHARGE - 1

        assertThrows(Exception::class.java) {
            sut.charge(amount)
        }
    }

    @Test
    fun `point 충전 시, 충전 최대 금액보다 크면 실패`(){
        val amount = UserPoint.MAX_AMOUNT_PER_CHARGE + 1

        assertThrows(Exception::class.java) {
            sut.charge(amount)
        }
    }

    @Test
    fun `point 충전 후, 총 point가 최대 보유 금액을 넘으면 실패`(){
        sut.point = (UserPoint.MAX_AMOUNT_PER_CHARGE..UserPoint.MAX_AMOUNT_PER_CHARGE).random()

        assertThrows(Exception::class.java) {
            sut.charge(UserPoint.MAX_TOTAL_AMOUNT - sut.point + 1)
        }
    }

    @Test
    fun `point 사용 시, 사용 최소 금액보다 작으면 실패`(){
        val amount = UserPoint.MIN_AMOUNT_PER_USE - 1

        assertThrows(Exception::class.java) {
            sut.use(amount)
        }
    }

    @Test
    fun `point 사용 시, 사용 최대 금액보다 크면 실패`(){
        val amount = UserPoint.MAX_AMOUNT_PER_USE + 1

        assertThrows(Exception::class.java) {
            sut.use(amount)
        }
    }

    @Test
    fun `point 사용 시, 시용 금액이 가진 금액보다 많으면 실패`(){
        val amount = (sut.point + 1..UserPoint.MAX_AMOUNT_PER_USE).random()

        assertThrows(Exception::class.java) {
            sut.use(amount)
        }
    }
}