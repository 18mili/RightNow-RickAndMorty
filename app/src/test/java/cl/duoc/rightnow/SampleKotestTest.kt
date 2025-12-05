package cl.duoc.rightnow

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SampleKotestTest : StringSpec({

    "1 + 1 debe ser 2" {
        (1 + 1) shouldBe 2
    }
})
