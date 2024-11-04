package digital.hashbang.demo.conversion

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class ConversionRequest(val start: String, val end: String)
data class ConversionResponse(val startUnit: String, val endUnit: String, val ratio: Double)

@RestController
@RequestMapping("/convert")
class ConversionController(conversionService: ConversionService) {

    @PostMapping
    fun handleConversion(@RequestBody message: ConversionRequest) : ResponseEntity<ConversionResponse> {
        return ResponseEntity.ok(ConversionResponse("", "", 0.0))
    }
}

