package digital.hashbang.demo.conversion

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class ConversionRequest(@get:JsonProperty("start_unit") val startUnit: String,
                             @get:JsonProperty("end_unit") val endUnit: String)
data class ConversionResponse(val startUnit: String, val endUnit: String, val ratio: Float, val error: String)

@RestController
@RequestMapping("/convert")
class ConversionController(private val conversionService: ConversionService) {

    @PostMapping
    fun handleConversion(@RequestBody message: ConversionRequest) : ResponseEntity<ConversionResponse> {
        try {
            val conversionRate = conversionService.search(message.startUnit, message.endUnit)
            return ResponseEntity.ok(ConversionResponse(message.startUnit, message.endUnit, conversionRate, ""))
        } catch (e : IllegalArgumentException) {
            return ResponseEntity.badRequest().body(ConversionResponse(message.startUnit, message.endUnit, 0.0F,
                    "Could not not path between ${message.startUnit} -> ${message.endUnit}"))
        } catch (e : Exception) {
            return ResponseEntity.internalServerError().body(ConversionResponse(message.startUnit, message.endUnit, 0.0F,
                    "Could not process your request."))
        }
    }
}

