package io.hhplus.tdd.point

interface PointService {
    fun findUserPointById(id: Long): UserPoint

    fun findUserPointHistoryByUserId(userId: Long): List<PointHistory>

    fun charge(userId: Long, amount: Long): UserPoint

    fun use(userId: Long, amount: Long): UserPoint
}