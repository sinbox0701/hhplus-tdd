package io.hhplus.tdd.point

data class UserPoint(
    val id: Long,
    var point: Long,
    val updateMillis: Long,
){
    companion object CONSTANTS {
        private const val ONE_HUNDRED_MILLION =  100000000L // 1억

        const val MAX_TOTAL_AMOUNT = ONE_HUNDRED_MILLION

        const val MIN_AMOUNT_PER_CHARGE = 1L
        const val MAX_AMOUNT_PER_CHARGE = ONE_HUNDRED_MILLION

        const val MIN_AMOUNT_PER_USE = 1L
        const val MAX_AMOUNT_PER_USE = ONE_HUNDRED_MILLION
    }

    fun charge(amount: Long) {
        if (amount < MIN_AMOUNT_PER_CHARGE || amount > MAX_AMOUNT_PER_CHARGE)
            throw Exception("Recharge is possible from 1 point to 100 million points.")


        if (point + amount > MAX_TOTAL_AMOUNT)
            throw Exception("You have exceeded the maximum rechargeable points.")

        point += amount
    }

    fun use(amount: Long) {
        if (amount < MIN_AMOUNT_PER_USE || amount > MAX_AMOUNT_PER_USE)
            throw Exception("Usage is possible from 1 point to 100 million points.")


        if (point < amount)
            throw Exception("You cannot use more points than you have.")


        point -= amount

    }
}
