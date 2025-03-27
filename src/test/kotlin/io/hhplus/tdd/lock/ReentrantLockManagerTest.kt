package io.hhplus.tdd.lock

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ReentrantLockManagerTest {
    private val sut = ReentrantLockManager()

    @Test fun `lock없이 증가 연산자를 사용하면 실패`() {
        //given
        var count = 0L
        val expected = 1000L

        //when
        ConcurrencyAssistant.execute(expected) {
            count++
        }

        //then
        assert(count != expected)
    }

    @Test fun `lock을 이용해 중가 연산자를 사용하면 성공`() {
        //given
        var count = 0L
        val expected = 1000L

        //when
        ConcurrencyAssistant.execute(expected) {
            sut.execute(1L) { count++ }
        }

        //then
        assertEquals(expected, count)
    }
}