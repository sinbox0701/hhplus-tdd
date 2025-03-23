package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import org.springframework.stereotype.Service

@Service
class PointServiceImpl(val userPointTable: UserPointTable, val pointHistoryTable: PointHistoryTable): PointService {
    override fun findUserPointById(id: Long): UserPoint {
        return userPointTable.selectById(id)
    }

    override fun findUserPointHistoryByUserId(userId: Long): List<PointHistory> {
        return pointHistoryTable.selectAllByUserId(userId)
    }

    override fun charge(userId: Long, amount: Long): UserPoint {
        val userPoint = userPointTable.selectById(userId)
        userPoint.charge(amount)
        pointHistoryTable.insert(userId, amount, TransactionType.CHARGE, System.currentTimeMillis())
        userPointTable.insertOrUpdate(userId, userPoint.point)
        return userPoint
    }

    override fun use(userId: Long, amount: Long): UserPoint {
        val userPoint = userPointTable.selectById(userId)
        userPoint.use(amount)
        pointHistoryTable.insert(userId, amount, TransactionType.USE, System.currentTimeMillis())
        userPointTable.insertOrUpdate(userId, userPoint.point)
        return userPoint
    }
}