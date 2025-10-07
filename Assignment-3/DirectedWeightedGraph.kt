/**
 * Graph interface
 *
 * @param VertexType used to represent each vertex in the graph
 */
interface Graph<VertexType> {
    /**
     * Returns all vertices in the graph
     *
     * @return a set containing all vertices
     */
    fun getVertices(): Set<VertexType>
    /**
     * Adds a directed edge to a specific weight
     * If the vertices do not exist, they are added to the graph
     *
     * @param from the starting vertex of the edge
     * @param to the vertex of the edge
     * @param weight the weight of traversing the edge
     */
    fun addEdge(from: VertexType, to: VertexType, weight: Double)

    /**
     * Returns a map of all outgoing edges from given vertex
     *
     * @param from the vertex whose outgoing edges should be returned
     * @return a Map where each key is a neighbor vertex and each value is the edge’s weight
     *         Returns an empty map if from has no outgoing edges or does not exist
     */
    fun getEdges(from: VertexType): Map<VertexType, Double>
    /**
     * Removes all vertices and edges from the graph, leaving it empty.
     */
    fun clear()
}

/**
 * Directed weighted graph using an adjacency list
 *
 * @param VertexType the type used to represent each vertex
 * @property vertices holds all vertices in the graph without duplicates
 * @property adjacencyList maps each vertex to its outgoing neighbors and edge weights
 */
class DirectedWeightedGraph<VertexType> : Graph<VertexType> {

    /** Holds all unique vertices in the graph. */
    private val vertices: MutableSet<VertexType> = mutableSetOf()
    /**
     * The adjacency list representation of the graph
     * Each vertex maps to another map of neighbor → weight
     */
    private val adjacencyList: MutableMap<VertexType, MutableMap<VertexType, Double>> = mutableMapOf()


    override fun getVertices(): Set<VertexType> = vertices


    override fun addEdge(from: VertexType, to: VertexType, weight: Double) {
        require(weight >= 0) { "Edge weight must not be negative" }

        vertices.add(from)
        vertices.add(to)

        val neighbors = adjacencyList.getOrPut(from) { mutableMapOf() }
        neighbors[to] = weight
    }

    override fun getEdges(from: VertexType): Map<VertexType, Double> =
        adjacencyList[from]?.toMap() ?: emptyMap()

    override fun clear() {
        vertices.clear()
        adjacencyList.clear()
    }
}
