import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DirectedWeightedGraphTest {

    @Test
    fun getVertices() {
        val graph = DirectedWeightedGraph<String>()
        assertTrue(graph.getVertices().isEmpty(), "Graph should start empty")

        graph.addEdge("A", "B", 2.0)
        graph.addEdge("B", "C", 3.5)

        val vertices = graph.getVertices()
        assertEquals(setOf("A", "B", "C"), vertices, "Vertices should include all endpoints")
    }

    @Test
    fun addEdge() {
        val graph = DirectedWeightedGraph<String>()

        graph.addEdge("X", "Y", 5.0)
        assertTrue("X" in graph.getVertices())
        assertTrue("Y" in graph.getVertices())

        val edgesFromX = graph.getEdges("X")
        assertEquals(1, edgesFromX.size)
        assertEquals(5.0, edgesFromX["Y"])

        // update existing edge
        graph.addEdge("X", "Y", 7.0)
        assertEquals(7.0, graph.getEdges("X")["Y"], "Edge cost should update when re-adding")
    }

    @Test
    fun getEdges() {
        val graph = DirectedWeightedGraph<String>()
        graph.addEdge("A", "B", 4.0)
        graph.addEdge("A", "C", 6.0)
        graph.addEdge("C", "D", 1.5)

        val edgesFromA = graph.getEdges("A")
        assertEquals(2, edgesFromA.size)
        assertEquals(4.0, edgesFromA["B"])
        assertEquals(6.0, edgesFromA["C"])

        val edgesFromC = graph.getEdges("C")
        assertEquals(1, edgesFromC.size)
        assertEquals(1.5, edgesFromC["D"])

        val edgesFromB = graph.getEdges("B")
        assertTrue(edgesFromB.isEmpty(), "Vertex with no outgoing edges should return empty map")
    }
}
