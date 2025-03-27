package io.hhplus.tdd.point

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.random.Random
import org.junit.jupiter.api.Assertions.assertEquals


class PointServiceIntegration {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Autowired
    private lateinit var sut: PointServiceImpl

    @Test
    fun `충전, 사용, 조회 통합 테스트`(){
        // Charge - Given
        val userId = Random.nextLong();
        val userPoint = sut.findUserPointById(userId)
        val account = userPoint.point
        val amount = Random.nextLong(from = UserPoint.MIN_AMOUNT_PER_CHARGE, until = UserPoint.MAX_AMOUNT_PER_CHARGE - account)

        val total = sut.charge(userId, amount)
        val userPoint2 = sut.findUserPointById(userId)
        val userPointHistory = sut.findUserPointHistoryByUserId(userId)

        // Charge - Then
        assertEquals(account + amount, total.point)
        assertEquals(account + amount, userPoint2.point)
        if(userPointHistory.isEmpty()) fail("pointHistory should not be empty")
        val lastHistory = userPointHistory.maxBy { it.id }
        assertEquals(userId, lastHistory.userId)
        assertEquals(amount, lastHistory.amount)
        assertEquals(TransactionType.CHARGE, lastHistory.type)
        assertEquals(userPoint2.updateMillis, lastHistory.timeMillis)

        // Use - Given
        val account2 = userPoint2.point
        val amountUse = Random.nextLong(until = account2)

        // Use - When
        val pointAfterUse1 = sut.use(userId, amountUse)
        val pointAfterUse2 = sut.findUserPointById(userId)
        val userPointHistory2 = sut.findUserPointHistoryByUserId(userId)

        // Charge - Then
        assertEquals(account2 - amountUse, pointAfterUse1.point)
        assertEquals(account2 - amountUse, pointAfterUse2.point)
        if(userPointHistory.size >= 2) fail("pointHistory should not be empty")
        val lastUseHistory = userPointHistory2.maxBy { it.id }
        assertEquals(userId, lastUseHistory.userId)
        assertEquals(amountUse, lastUseHistory.amount)
        assertEquals(TransactionType.USE, lastUseHistory.type)
        assertEquals(pointAfterUse1.updateMillis, lastUseHistory.timeMillis)
    }

    enum class CommandType{ USE, CHARGE}
    class Command(
        val type: CommandType,
        val amount: Long,
    )

    @Test fun `충전 동시성 테스트`() {
        //given
        val threadSize = 10
        val userId = 1L
        val amount = 50L

        val executorService = Executors.newFixedThreadPool(threadSize)
        val doneSignal = CountDownLatch(threadSize)

        //when
        for (i in 1..threadSize) {
            executorService.execute {
                sut.charge(userId, amount)
                doneSignal.countDown()
            }
        }

        doneSignal.await()
        executorService.shutdown()

        val result = sut.findUserPointById(userId)

        //then
        assertEquals(amount * threadSize, result.point)
    }

    @Test fun `사용 동시성 테스트`() {
        //given
        val threadSize = 5
        val userId = 1L
        val amount = 50L
        sut.charge(userId, amount * threadSize)

        val executorService = Executors.newFixedThreadPool(threadSize)
        val doneSignal = CountDownLatch(threadSize)

        //when
        for (i in 1..threadSize) {
            executorService.execute {
                sut.use(userId, amount)
                doneSignal.countDown()
            }
        }

        doneSignal.await()
        executorService.shutdown()

        val result = sut.findUserPointById(userId)

        //then
        assertEquals(0, result.point)
    }
}