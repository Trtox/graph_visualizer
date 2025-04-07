package trtox.graphvisualizer.logic

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class VerticesTest {

    @Test
    fun `test adding unique vertices`() {
        val vertices = Vertices()
        val vertexA = Vertex(1, "A")
        val vertexB = Vertex(2, "B")

        vertices.addVertex(vertexA)
        vertices.addVertex(vertexB)

        assertEquals(2, vertices.size())  // Should contain both unique vertices
    }

    @Test
    fun `test adding duplicate vertex does not increase size`() {
        val vertices = Vertices()
        val vertexA = Vertex(1, "A")

        vertices.addVertex(vertexA)
        vertices.addVertex(vertexA)  // Duplicate, should be ignored

        assertEquals(1, vertices.size())  // Still should be 1
    }

    @Test
    fun `test removing existing vertex`() {
        val vertices = Vertices()
        val vertexA = Vertex(1, "A")
        vertices.addVertex(vertexA)

        vertices.removeVertex(vertexA)
        assertEquals(0, vertices.size())  // Should be empty after removal
    }

    @Test
    fun `test removing non-existent vertex does nothing`() {
        val vertices = Vertices()
        val vertexA = Vertex(1, "A")

        vertices.removeVertex(vertexA)  // should not fail or affect size
        assertEquals(0, vertices.size())
    }

    @Test
    fun `test contains method`() {
        val vertices = Vertices()
        val vertexA = Vertex(1, "A")
        vertices.addVertex(vertexA)

        assertTrue(vertices.contains(vertexA))  // should contain added vertex
        assertFalse(vertices.contains(Vertex(2, "B")))  // should not contain this
    }

    @Test
    fun `test get vertex by label`() {
        val vertices = Vertices()
        val vertexA = Vertex(1, "A")
        vertices.addVertex(vertexA)

        val foundVertex = vertices.getVertexByLabel("A")
        assertNotNull(foundVertex)
        assertEquals(vertexA, foundVertex)
    }

    @Test
    fun `test get vertex by label returns null if not found`() {
        val vertices = Vertices()
        val result = vertices.getVertexByLabel("X")

        assertNull(result)
    }

    @Test
    fun `test get all vertices`() {
        val vertices = Vertices()
        val vertexA = Vertex(1, "A")
        val vertexB = Vertex(2, "B")

        vertices.addVertex(vertexA)
        vertices.addVertex(vertexB)

        val allVertices = vertices.getAllVertices()
        assertEquals(setOf(vertexA, vertexB), allVertices)
    }

    @Test
    fun `test size function`() {
        val vertices = Vertices()
        assertEquals(0, vertices.size())  // should be empty initially

        vertices.addVertex(Vertex(1, "A"))
        assertEquals(1, vertices.size())

        vertices.addVertex(Vertex(2, "B"))
        assertEquals(2, vertices.size())
    }
}