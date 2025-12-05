package cl.duoc.rightnow

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ValidationTest {

    @Test
    fun `email valido debe retornar true`() {
        val result = isValidEmail("test@example.com")
        assertTrue(result)
    }

    @Test
    fun `email invalido debe retornar false`() {
        val result = isValidEmail("no-es-correo")
        assertFalse(result)
    }

    @Test
    fun `contrasena menor a 6 caracteres debe fallar`() {
        val result = isMinLength("abc", 6)
        assertFalse(result)
    }
}
