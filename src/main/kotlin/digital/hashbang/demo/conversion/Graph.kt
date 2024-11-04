package digital.hashbang.demo.conversion

import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap

class Graph(inputFileStream: InputStream) {
    private val graph = HashMap<String, HashMap<String, Float>>()
    private val inputCSVNode = readMeasurementsCSV(inputFileStream)
    private val nodesToVisit = ArrayDeque<Pair<String, Float>>()

    init {
        inputCSVNode.forEach{
            addConversion(it.first, it.second, it.third)
        }
    }

    fun addUnitRatio(startUnit: String, endUnit :String, ratio: Float) {
        this.graph[startUnit]?.set(endUnit, ratio)
    }

    fun getNodes(): List<String> {
        return this.graph.map { it.key }
    }

    fun bfSearch (startNode: String, endNode: String) : Float {
        nodesToVisit.add(Pair(startNode.toLowerCase(), 1.0F))
        val visitedNodes = mutableSetOf<String>()
        while (!nodesToVisit.isEmpty()) {
            val (node, rateFromOrigin) = nodesToVisit.remove()
            if (node == endNode.toLowerCase()) {
                return rateFromOrigin
            }
            visitedNodes.add(node)
            getNeighbors(node).forEach{
                if (!visitedNodes.contains(it.first)) {
                    nodesToVisit.add(Pair(it.first, it.second * rateFromOrigin))
                }
            }
        }
        return 1.0F
    }

    private fun addConversion(origin: String, dest: String, rate : Float) {
        if (!this.graph.containsKey(origin)) {
            this.graph[origin] = hashMapOf()
        }
        this.graph[origin]?.set(dest, rate)

        // Reverse the graph connection
        // Meters -> 0.3.. -> Feet
        // Feet -> 1/0.3... -> Meters
        if (!this.graph.containsKey(dest)){
            this.graph[dest] = hashMapOf()
        }
        this.graph[dest]?.set(origin, 1.0F / rate)
    }

    private fun getNeighbors(node: String): List<Pair<String, Float>> {
        val nodes = arrayListOf<Pair<String, Float>>()
        if (graph[node] == null) {
            return nodes
        }
        graph[node]?.forEach { (unit, ratio) ->
            nodes.add(Pair(unit, ratio))
        }
        return nodes
    }
}

fun readMeasurementsCSV(inputStream: InputStream): List<Triple<String, String, Float>> {
    val reader = inputStream.bufferedReader()
    val HEADER_NOT_USED = reader.readLine()
    return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (originUnit, destUnit, ratio) = it.split(',', ignoreCase = false, limit = 3)
                Triple(originUnit.toLowerCase(), destUnit.toLowerCase(), ratio.toFloat())
            }.toList()
}
