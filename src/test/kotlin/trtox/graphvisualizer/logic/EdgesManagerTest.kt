package trtox.graphvisualizer.logic

import javafx.application.Platform
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.nio.file.Files
import java.nio.file.Paths

import trtox.graphvisualizer.ui.VerticesUI

class EdgesManagerTest {
    private lateinit var manager : EdgesManager
    private val vertices : MutableSet<Vertex> = mutableSetOf()

    private fun readEdgesFromFile(): String {
        val path = Paths.get("src/test/resources/test_edges.txt")
        return Files.readString(path)
    }

    @BeforeEach
    fun setUp() {
        vertices.clear()

        manager = EdgesManager(
            onEdgesChange = { _ -> },
            vertices = VerticesUI { label, state -> manager.handleVertexStatusChange(label, state) }
        )
    }

    @Test
    fun `test handling new edges`() {
        val testInput = readEdgesFromFile()

        manager.handleTextChanged(testInput)

        val enabledEdges = manager.getEnabledEdges()
        assertEquals(104, enabledEdges.size)

        var edgesFile = readEdgesFromFile()

        var enabledEdgesString = ""
        enabledEdges.forEach {
            enabledEdgesString += it.toString() + "\n"
        }

        edgesFile += "\n"

        assertEquals(edgesFile, enabledEdgesString)
    }

    @Test
    fun `test handling status change of vertex`() {
        manager.handleTextChanged(readEdgesFromFile())
        manager.handleVertexStatusChange("A", false)

        val enabledEdges = manager.getEnabledEdges()
        assertEquals(96, enabledEdges.size)
        assertFalse(enabledEdges.contains(Edge(Vertex(1, "A"), Vertex(2, "B"))))
    }

    @Test
    fun `test getting enabled edges`() {
        manager.handleTextChanged(readEdgesFromFile())
        val edges = manager.getEnabledEdges()

        assertEquals(104, edges.size)
        assertTrue(edges.any { it.leftVertex.label == "A" && it.rightVertex.label == "B" })
        assertTrue(edges.any { it.leftVertex.label == "I" && it.rightVertex.label == "K" })
    }

    @Test
    fun `test if onEdgesChange is called after vertex state is changed`() {
        var called = false
        val testManager = EdgesManager(
            onEdgesChange = { called = true },
            vertices = VerticesUI { _, _ -> }
        )

        testManager.handleTextChanged(readEdgesFromFile())
        testManager.handleVertexStatusChange("0", false)

        assertTrue(called)
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun initToolkit() {
            Platform.startup {}
        }
    }
}