package trtox.graphvisualizer.logic

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import trtox.graphvisualizer.ui.VerticesUI

class VerticesManagerTest {
    private lateinit var manager: VerticesManager

    @BeforeEach
    fun setUp() {
     manager = VerticesManager()
    }

    @Test
    fun `test adding vertices, duplicate ids and labels`() {
     val v1 = Vertex(1, "A")
     val v2 = Vertex(2, "B")
     val v3 = Vertex(3, "A")
     val v4 = Vertex(4, "B")
     val v5 = Vertex(2, "C")
     val v6 = Vertex(5, "A")
     v6.isEnabled = false
     manager.addVertex(v1)
     manager.addVertex(v2)
     assertTrue(manager.getAllVertices().contains(v1) && manager.getAllVertices().contains(v2)) // basic adding

     manager.addVertex(v3)
     manager.getAllVertices().find {
      it.label == v3.label
     }?.let { assertEquals(it.degree, 2) } // should be 2, v3's label is same as v1

     assertFalse(manager.getAllVertices().contains(v4)) // should be false, v4 wasn't added

     manager.addVertex(v5)
     manager.addVertex(v6)
     assertFalse(manager.getAllVertices().contains(v6)) // should be false, v6 is disabled
     assertEquals(manager.getVertexCount(), 3) // should be 3, adding vertex with same id is irrelevant
    }

    @Test
    fun `test removing vertices, duplicates and non-existing`() {
     manager.addVertex(Vertex(1, "A"))
     manager.addVertex(Vertex(2, "B"))
     manager.addVertex(Vertex(3, "C"))

     manager.removeVertexByLabel("O")
     assertEquals(manager.getVertexCount(), 3) // should be 3, removing non-existing vertex shouldn't change anything

     manager.removeVertexByLabel("B")
     assertEquals(manager.getVertexCount(), 2) // should be 2, removing existing vertex should change size

     manager.removeVertexByLabel("B")
     assertEquals(manager.getVertexCount(), 2) // should be 2, removing same vertex shouldn't change size
    }

    @Test
    fun `test changing vertices states`() {
     manager.addVertex(Vertex(1, "A"))
     manager.addVertex(Vertex(2, "B"))
     manager.addVertex(Vertex(3, "C"))

     manager.setVertexState("D", false)
     manager.getAllVertices().forEach {
      assertEquals(it.isEnabled, true) // should be true, changing non-existing vertex's state shouldn't change anything
     }

     manager.setVertexState("B", false)
     manager.getAllVertices().find {
      it.label == "B"
     }?.let { assertEquals(it.isEnabled, false) } // should return false, state is changed
    }

    @Test
    fun `test getting vertex by label`() {
     manager.addVertex(Vertex(1, "A"))
     manager.addVertex(Vertex(2, "B"))
     manager.addVertex(Vertex(3, "C"))

     assertEquals(manager.getVertexByLabel("F"), null) // should be null, vertex doesn't exist
     manager.getVertexByLabel("B")?.let { assertEquals(it.id, 2) } // should get vertex with id 2 and label B
    }

    @Test
    fun `test getting all vertices`() {
     manager.addVertex(Vertex(1, "A"))
     manager.addVertex(Vertex(2, "B"))
     manager.addVertex(Vertex(3, "C"))
     manager.addVertex(Vertex(4, "D"))
     manager.addVertex(Vertex(5, "A"))

     assertEquals(manager.getAllVertices().size, 4) // should be 4, there are 4 different vertices added
    }

    @Test
    fun `test checking vertices count`() {
     manager.addVertex(Vertex(1, "A"))
     manager.addVertex(Vertex(2, "B"))
     manager.addVertex(Vertex(3, "C"))
     manager.addVertex(Vertex(4, "D"))
     manager.removeVertexByLabel("B")

     assertEquals(manager.getVertexCount(), 3) // should be 3, one vertex was removed
    }
}