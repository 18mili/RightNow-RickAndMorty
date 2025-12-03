package cl.duoc.rightnow

private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")

fun isNotBlank(value: String) = value.trim().isNotEmpty()

fun isValidEmail(value: String) = EMAIL_REGEX.matches(value.trim())

fun isMinLength(value: String, min: Int) = value.length >= min

fun passwordsMatch(p1: String, p2: String) = p1 == p2
