package trtox.graphvisualizer.ui

import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.input.ContextMenuEvent
import javafx.scene.layout.FlowPane
import javafx.scene.layout.VBox
import trtox.graphvisualizer.logic.Vertex
import trtox.graphvisualizer.logic.VerticesManager

/**
 * User interface component for managing and displaying graph vertices.
 *
 * This class allows users to interact with vertices in a graph by enabling or disabling them. It maintains a
 * list of vertices and displays them as labels in a `FlowPane`. Each vertex label has a context menu to enable
 * or disable the vertex, which affects the graph visualization.
 *
 * Vertices are managed by the `VerticesManager`, which handles the logic for adding, removing, and updating vertices.
 * The UI updates automatically when the state of the vertices changes, and the vertex count is displayed at the top.
 *
 * The class provides a callback (`onVertexStatusChange`) that is triggered whenever a vertex's state (enabled/disabled)
 * is changed by the user.
 */
class VerticesUI(
    var onVertexStatusChange: (String, Boolean) -> Unit
) : VBox() {
    private val verticesManager: VerticesManager = VerticesManager()
    private val vertexCountLabel: Label = Label("Vertex Counter: 0")
    private val vertexLabelsContainer: FlowPane = FlowPane()

    init {
        spacing = 10.0
        padding = Insets(10.0)
        vertexLabelsContainer.hgap = 10.0
        vertexLabelsContainer.vgap = 10.0
        vertexLabelsContainer.prefWrapLength = 300.0
        children.addAll(vertexCountLabel, vertexLabelsContainer)
    }

    /**
     * Adds a [vertex] to the UI and updates the vertex counter.
     */
    fun addVertex(vertex: Vertex) {
        if (!verticesManager.addVertex(vertex)) return

        val vertexLabel = createVertexLabel(vertex)
        vertexLabelsContainer.children.add(vertexLabel)
        updateVertexCounterLabel()
    }

    /**
     * Removes a vertex from the UI by its [label] and updates the vertex counter.
     */
    fun removeVertexByLabel(label: String) {
        if (!verticesManager.removeVertexByLabel(label)) return

        val labelToRemove = vertexLabelsContainer.children
            .filterIsInstance<Label>()
            .find { it.text == label }
        labelToRemove?.let { vertexLabelsContainer.children.remove(it) }

        updateVertexCounterLabel()
    }

    /**
     * Gets a vertex by its [label]
     */
    fun getVertexByLabel(label: String): Vertex? = verticesManager.getVertexByLabel(label)

    /**
     * Gets the current count of vertices.
     */
    fun getVertexCount(): Int = verticesManager.getVertexCount()

    /**
     * Gets all vertices managed by this UI component as a set.
     */
    fun getAllVertices(): Set<Vertex> = verticesManager.getAllVertices()

    /**
     * Sets the state of a vertex with [label] to [newState] (enabled/disabled).
     */
    fun setVertexLabelState(label: String, newState: Boolean): Boolean = verticesManager.setVertexState(label, newState)

    /**
     * Creates a label for a [vertex] that includes a context menu for enabling or disabling the vertex.
     */
    private fun createVertexLabel(vertex: Vertex): Label {
        val vertexLabel = Label(vertex.label)
        vertexLabel.style = getVertexStyle(vertex.isEnabled)

        val contextMenu = ContextMenu().apply {
            items.addAll(
                MenuItem("Enable").apply {
                    setOnAction {
                        verticesManager.setVertexState(vertex.label, true)
                        vertexLabel.style = getVertexStyle(true)
                        onVertexStatusChange(vertex.label, true)
                    }
                },
                MenuItem("Disable").apply {
                    setOnAction {
                        verticesManager.setVertexState(vertex.label, false)
                        vertexLabel.style = getVertexStyle(false)
                        onVertexStatusChange(vertex.label, false)
                    }
                }
            )
        }

        vertexLabel.setOnContextMenuRequested { event: ContextMenuEvent ->
            contextMenu.show(vertexLabel, event.screenX, event.screenY)
        }

        return vertexLabel
    }

    /**
     * Gets the appropriate style for a vertex label based on its state [isEnabled].
     */
    private fun getVertexStyle(isEnabled: Boolean): String = """
        -fx-padding: 5px;
        -fx-background-color: ${if (isEnabled) "green" else "red"};
        -fx-border-radius: 5px;
        -fx-background-radius: 5px;
        -fx-text-fill: black;
    """.trimIndent()

    /**
     * Updates the vertex counter label to display the current number of vertices.
     */
    private fun updateVertexCounterLabel() {
        vertexCountLabel.text = "Vertex Counter: ${verticesManager.getVertexCount()}"
    }
}
