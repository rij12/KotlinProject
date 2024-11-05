package digital.hashbang.demo.conversion

import org.springframework.stereotype.Service
import java.io.File


@Service
class ConversionService(config: Configuration) {

    private final val fileStream = File(config.measurementsInputFile).inputStream()
    val nodeGraph = Graph(fileStream)

    fun search(originUnit: String, destinationUnit: String): Float {
        val ratio = nodeGraph.bfSearch(originUnit.toLowerCase(), destinationUnit.toLowerCase())
        require(ratio != 0.0F) { "Unit does not exist" }
        return ratio
    }

}
