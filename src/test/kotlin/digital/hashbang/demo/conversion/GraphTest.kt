package digital.hashbang.demo.conversion

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource

class UnitConversionTests {

    private val csvFile = ClassPathResource("units.csv")
    private val conversionGraph = Graph(csvFile.inputStream)

    @Test
    fun testBFSearch() {
        val feetToMeters = conversionGraph.bfSearch("Feet", "Meters")
        print(feetToMeters)
        assertEquals(3.28084F, feetToMeters)

        val RodToHand = conversionGraph.bfSearch("Rods", "Hands")
        print(RodToHand)
        assertEquals(0.02020202F, RodToHand)

    }
}
