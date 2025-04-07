package trtox.graphvisualizer.logic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EdgesTest {

    private lateinit var edges: Edges
    private lateinit var vertexA: Vertex
    private lateinit var vertexB: Vertex
    private lateinit var edgeAB: Edge

    @BeforeEach
    fun setup() {
     edges = Edges()
     vertexA = Vertex(1,"A")
     vertexB = Vertex(2,"B")
     edgeAB = Edge(vertexA, vertexB)
    }

    @Test
    fun `addEdge should add an edge to the set`() {
     assertTrue(edges.addEdge(edgeAB))
     assertTrue(edges.contains(edgeAB))
    }

    @Test
    fun `contains should return true for existing edges`() {
     edges.addEdge(edgeAB)
     assertTrue(edges.contains(edgeAB))
    }

    @Test
    fun `contains should return false for non-existent edges`() {
     assertFalse(edges.contains(edgeAB))
    }

    @Test
    fun `removeAllEdges should clear all edges`() {
     edges.addEdge(edgeAB)
     edges.removeAllEdges()
     assertFalse(edges.contains(edgeAB))
    }

    @Test
    fun `getEnabledEdges should return only enabled edges`() {
     edges.addEdge(edgeAB)
     assertEquals(setOf(edgeAB), edges.getEnabledEdges())

     vertexA.isEnabled = false
     edges.updateEdgesForVertex("A", false)
     assertTrue(edges.getEnabledEdges().isEmpty())
    }

    @Test
    fun `updateEdgesForVertex should correctly enable or disable edges`() {
     edges.addEdge(edgeAB)

     assertTrue(edgeAB.isEnabled)

     edges.updateEdgesForVertex("A", false)
     assertFalse(edgeAB.isEnabled)

     edges.updateEdgesForVertex("A", true)
     assertTrue(edgeAB.isEnabled)
    }

}