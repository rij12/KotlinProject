package digital.hashbang.demo.conversion

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {
    @Value("\${converter.filepath}")
    lateinit var measurementsInputFile: String
}