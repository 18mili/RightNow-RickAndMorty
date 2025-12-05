package cl.duoc.rightnow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CoroutineBehaviorTest {

    @Test
    fun `coroutine se ejecuta en test dispatcher`() = runTest {
        var value = 0

        launch {
            delay(100)
            value = 10
        }

        // Avanza TODAS las tareas pendientes
        advanceUntilIdle()

        // Ahora sí, value debería ser 10
        assertEquals(10, value)
    }
}
