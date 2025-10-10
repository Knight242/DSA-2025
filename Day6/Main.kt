class Graph<VertexType> {
    private var vertices: MutableSet<VertexType> = mutableSetOf()
    private var edges: MutableMap<VertexType, MutableSet<VertexType>> = mutableMapOf()

    /**
     * Add the vertex [v] to the graph
     * @param v the vertex to add
     * @return true if the vertex is successfully added, false if the vertex
     *   was already in the graph
     */
    fun addVertex(v: VertexType): Boolean {
        if (vertices.contains(v)) {
            return false
        }
        vertices.add(v)
        return true
    }

    /**
     * Add an edge between vertex [from] connecting to vertex [to]
     * @param from the vertex for the edge to originate from
     * @param to the vertex to connect the edge to
     * @return true if the edge is successfully added and false if the edge
     *     can't be added or already exists
     */
    fun addEdge(from: VertexType, to: VertexType): Boolean {
        if (!vertices.contains(from) || !vertices.contains(to)) {
            return false
        }
        edges[from]?.also { currentAdjacent ->
            if (currentAdjacent.contains(to)) {
                return false
            }
            currentAdjacent.add(to)
        } ?: run {
            edges[from] = mutableSetOf(to)
        }
        return true
    }

    /**
     * Clear all vertices and edges
     */
    fun clear() {
        vertices = mutableSetOf()
        edges = mutableMapOf()
    }
    // Excersize 3
    fun bfs(start: VertexType, target: VertexType): Boolean {
        if (start !in vertices || target !in vertices) {
            return false
        }

        val toVisit = mutableSetOf<VertexType>()       // tracks visited vertices
        val queue: ArrayDeque<VertexType> = ArrayDeque()

        queue.add(start)
        toVisit.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current == target) {
                return true // found a path!
            }

            val neighbors = edges[current] ?: emptySet()
            for (neighbor in neighbors) {
                if (neighbor !in toVisit) {
                    queue.add(neighbor)
                    toVisit.add(neighbor)
                }
            }
        }

        return false // no path found
    }
}

// Excersize 4
/*
fun dfs(start: VertexType, target: VertexType): Boolean {
    if (start !in vertices || target !in vertices) {
        return false
    }
    val toVisit = mutableSetOf<VertexType>()     // visited nodes
    val stack = ArrayDeque<VertexType>()

    stack.add(start)
    toVisit.add(start)

    while (stack.isNotEmpty()) {
        val current = stack.removeLast()         // pop from top of stack
        if (current == target) {
            return true // path found
        }
        val neighbors = edges[current] ?: emptySet()
        for (neighbor in neighbors) {
            if (neighbor !in toVisit) {
                stack.add(neighbor)              // push onto stack
                toVisit.add(neighbor)
            }
        }
    }
    return false
}
 */