fun main() {
    val cityGraph = DirectedWeightedGraph<String>()

    // Bidirectional roads
    fun addRoad(a: String, b: String, miles: Double) {
        cityGraph.addEdge(a, b, miles)
        cityGraph.addEdge(b, a, miles)
    }

    // Cities
    addRoad("Boston", "New York", 215.0)
    addRoad("Boston", "Albany", 170.0)
    addRoad("New York", "Philadelphia", 95.0)
    addRoad("Philadelphia", "Washington DC", 123.0)
    addRoad("Albany", "Buffalo", 290.0)
    addRoad("Buffalo", "Cleveland", 190.0)
    addRoad("Cleveland", "Chicago", 345.0)
    addRoad("New York", "Cleveland", 460.0)
    addRoad("Chicago", "St. Louis", 300.0)
    addRoad("Washington DC", "Atlanta", 640.0)

    // Shortest path examples
    val p1 = dijkstraShortestPath(cityGraph, "Boston", "Chicago")
    val p2 = dijkstraShortestPath(cityGraph, "Boston", "Atlanta")
    val p3 = dijkstraShortestPath(cityGraph, "Albany", "St. Louis")

    println("Shortest Boston → Chicago: $p1")
    println("Shortest Boston → Atlanta: $p2")
    println("Shortest Albany → St. Louis: $p3")
}
