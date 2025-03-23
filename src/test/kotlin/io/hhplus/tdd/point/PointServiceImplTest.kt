package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryFakeTable
import io.hhplus.tdd.database.UserPointFakeTable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class PointServiceImplTest {
    private val userId = 1L

    private val pointHistoryTable = PointHistoryFakeTable()
    private val userPointTable = UserPointFakeTable()

    private val sut: PointServiceImpl = PointServiceImpl(userPointTable,pointHistoryTable)

    @Test fun `입력 받은 id로 UserPoint 조회 시, 해당 id의 UserPoint 반환`(){
        val userPoint = sut.findUserPointById(userId)

        assertEquals(userId, userPoint.id)
    }

    @Test fun `입력 받은 userId로 pointHistory 조회 시, 해당 userId를 가지고있는 pointHistory List 반환`(){
        val pointHistories = sut.findUserPointHistoryByUserId(userId)

        for (pointHistory in pointHistories) {
            assertEquals(userId, pointHistory.userId)
        }
    }

    @Test fun `충전 금액이 최소 충전 금액과 최대 충전 금액사이에 있고, 충전 후 금액이 최대 보유 가능 금액보다 작으면, 충전 금액만큼 포인트가 증가한다`() {
        val userPoint = sut.findUserPointById(userId)
        val amount = (UserPoint.MIN_AMOUNT_PER_CHARGE..UserPoint.MAX_AMOUNT_PER_CHARGE - userPoint.point).random()

        val result = sut.charge(userId, amount)

        assertEquals(userPoint.point + amount, result.point)

    }

    @Test fun `사용 금액이 최소 사용 금액과, 최대 사용 금액사이에 있고, 가지고 있는 point를 안 넘으면 사용 금액만큼 포인트가 감소한다`() {
        val userPoint = sut.findUserPointById(userId)
        val amount = (UserPoint.MIN_AMOUNT_PER_USE..Math.min(UserPoint.MAX_AMOUNT_PER_USE, userPoint.point)).random()

        val result = sut.use(userId, amount)

        assertEquals(userPoint.point - amount, result.point)
    }

}