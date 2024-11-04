package digital.hashbang.demo.conversion

import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap

//data class Node(val first: String, val rate: Float): Comparable<Node> {
//    override fun compareTo(other: Node): Int {
//        return this.rate.compareTo(other.rate)
//    }
//}

class Graph(inputFileStream: InputStream) {
    val graph = HashMap<String, HashMap<String, Float>>()
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
        nodesToVisit.add(Pair(startNode, 1.0F))
        val visitedNodes = mutableSetOf<String>()
        println("Start Node: $startNode")
        while (!nodesToVisit.isEmpty()) {
            val (node, rateFromOrigin) = nodesToVisit.remove()
            println("Currently at Node: $node with weight $rateFromOrigin")
            if (node == endNode) {
                println("EndNode : $endNode")
                return rateFromOrigin
            }
            visitedNodes.add(node)
            getNeighbors(node).forEach{
                if (!visitedNodes.contains(it.first)) {
                    println("Adding ${it.first}, with rate: ${it.second}")
                    nodesToVisit.add(Pair(it.first, it.second * rateFromOrigin))
                }
            }
        }
        return 0.0F
    }

    private fun addConversion(origin: String, dest: String, rate : Float) {
        // Add unit to graph
        if (!this.graph.containsKey(origin)) {
            this.graph[origin] = hashMapOf()
        }
        this.graph[origin]?.set(dest, rate)

        // Reverse the graph connection
        if (!this.graph.containsKey(dest)){
            this.graph[dest] = hashMapOf()
        }
        this.graph[dest]?.set(origin, 1.0F / rate)
    }

    fun getNeighbors(node: String): List<Pair<String, Float>> {
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

fun Pair<String, Float>.compareTo(p1 : Pair<String, Float>): Int {
    return this.second.compareTo(p1.second)
}

fun readMeasurementsCSV(inputStream: InputStream): List<Triple<String, String, Float>> {
    val reader = inputStream.bufferedReader()
    val header_NOT_USED = reader.readLine()
    return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (originUnit, destUnit, ratio) = it.split(',', ignoreCase = false, limit = 3)
                Triple(originUnit, destUnit, ratio.toFloat())
            }.toList()
}
