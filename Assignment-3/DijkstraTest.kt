import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DijkstraTest {

    @Test
    fun findsCorrectShortestPath() {
        val g = DirectedWeightedGraph<String>()
        g.addEdge("A", "B", 2.0)
        g.addEdge("B", "C", 2.0)
        g.addEdge("A", "C", 5.0)

        val path = dijkstraShortestPath(g, "A", "C")
        assertEquals(listOf("A", "B", "C"), path)
    }

    @Test
    fun returnNull() {
        val g = DirectedWeightedGraph<String>()
        g.addEdge("A", "B", 1.0)
        g.addEdge("C", "D", 1.0)

        val path = dijkstraShortestPath(g, "A", "D")
        assertNull(path)
    }

    @Test
    fun singleVertexPath() {
        val g = DirectedWeightedGraph<String>()
        g.addEdge("A", "B", 4.0)

        val path = dijkstraShortestPath(g, "A", "A")
        assertEquals(listOf("A"), path)
    }

    @Test
    fun directEdge() {
        val g = DirectedWeightedGraph<String>()
        g.addEdge("A", "B", 1.0)
        g.addEdge("A", "C", 2.0)
        g.addEdge("C", "B", 10.0)

        val path = dijkstraShortestPath(g, "A", "B")
        assertEquals(listOf("A", "B"), path)
    }
}
