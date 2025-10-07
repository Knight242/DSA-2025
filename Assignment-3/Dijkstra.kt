/**
 * Finds the shortest path between two vertices in a weighted directed graph
 * using Dijkstra’s
 *
 * Uses [PriorityQueue] to always look at each vertex
 * with the currently known "lowest" distance from [start]
 *
 * @param VertexType represents each vertex in the graph
 * @param graph contains vertices and weighted edges
 * @param start the starting vertex
 * @param destination the target vertex whose shortest path is to be found
 * @return a [List] of vertices representing the shortest path
 *
 * from start to finish, this includes both endpoints
 * returns `null` if no path exists
 *
 * Notes:
 * ->All edge weights must be non-negative
 * -> If [start] equals [destination], the result is a single-element list `[start]`
 * -> The path is rebuilt by tracing backward from [destination]
 */
fun <VertexType> dijkstraShortestPath(
    graph: Graph<VertexType>,
    start: VertexType,
    destination: VertexType
): List<VertexType>? {
    val dist = mutableMapOf<VertexType, Double>().withDefault { Double.POSITIVE_INFINITY }
    val prev = mutableMapOf<VertexType, VertexType?>()

    // Min-priority queue to pick the next closest vertex
    val pq = PriorityQueue<VertexType>()

    // Initialize distances
    for (v in graph.getVertices()) {
        dist[v] = Double.POSITIVE_INFINITY
        prev[v] = null
    }
    dist[start] = 0.0
    pq.addWithPriority(start, 0.0)

    // Main loop
    while (!pq.isEmpty()) {
        val u = pq.next() ?: break
        if (u == destination) break

        val uDist = dist.getValue(u)
        for ((neighbor, weight) in graph.getEdges(u)) {
            val alt = uDist + weight
            if (alt < dist.getValue(neighbor)) {
                dist[neighbor] = alt
                prev[neighbor] = u
                pq.addWithPriority(neighbor, alt)  // safe re-insertion
            }
        }
    }

    // No path found
    if (dist.getValue(destination) == Double.POSITIVE_INFINITY) return null

    // Reconstruct path
    val path = mutableListOf<VertexType>()
    var cur: VertexType? = destination
    while (cur != null) {
        path.add(cur)
        cur = prev[cur]
    }
    return path.reversed()
}
