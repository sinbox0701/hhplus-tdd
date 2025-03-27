package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.lock.ReentrantLockManager
import org.springframework.stereotype.Service

@Service
class PointServiceImpl(val lockManager: ReentrantLockManager, val userPointTable: UserPointTable, val pointHistoryTable: PointHistoryTable): PointService {
    override fun findUserPointById(id: Long): UserPoint {
        return userPointTable.selectById(id)
    }

    override fun findUserPointHistoryByUserId(userId: Long): List<PointHistory> {
        return pointHistoryTable.selectAllByUserId(userId)
    }

    override fun charge(userId: Long, amount: Long): UserPoint {
        return lockManager.execute(userId){
            val userPoint = userPointTable.selectById(userId)
            userPoint.charge(amount)
            pointHistoryTable.insert(userId, amount, TransactionType.CHARGE, System.currentTimeMillis())
            userPointTable.insertOrUpdate(userId, userPoint.point)
        }
    }

    override fun use(userId: Long, amount: Long): UserPoint {
        return lockManager.execute(userId){
            val userPoint = userPointTable.selectById(userId)
            userPoint.use(amount)
            pointHistoryTable.insert(userId, amount, TransactionType.USE, System.currentTimeMillis())
            userPointTable.insertOrUpdate(userId, userPoint.point)
        }
    }
}