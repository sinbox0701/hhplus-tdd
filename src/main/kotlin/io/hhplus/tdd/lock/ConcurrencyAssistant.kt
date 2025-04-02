package io.hhplus.tdd.lock

import java.util.concurrent.CompletableFuture

object ConcurrencyAssistant {
    fun execute(count: Long, action: () -> Unit) {
        val asyncTasks = (1..count).map {
            CompletableFuture.runAsync { action() }
        }
        CompletableFuture.allOf(*asyncTasks.toTypedArray()).join()
    }
}