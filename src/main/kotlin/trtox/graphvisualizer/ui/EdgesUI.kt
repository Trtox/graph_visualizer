package trtox.graphvisualizer.ui

import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import trtox.graphvisualizer.logic.Edge
import trtox.graphvisualizer.logic.EdgesManager

/**
 * User interface component for managing and displaying a list of graph edges.
 *
 * This class provides a text area where users can input graph edges in the format:
 * "A -> B", where A and B are vertex labels. The edges are parsed and managed by the `EdgesManager`.
 *
 * The class listens for changes to the text area and updates the graph's edges accordingly. It also reacts to
 * changes in the status of vertices by enabling or disabling related edges through the `EdgesManager`.
 *
 * The `EdgesUI` component interacts with the `VerticesUI` component to ensure consistency between the vertices and edges.
 * It also notifies the parent class of changes in the edges via the provided `onEdgesChange` callback.
 */
class EdgesUI(
    val onEdgesChange: (Set<Edge>) -> Unit,
    vertices: VerticesUI
) : VBox() {
    private val edgesManager: EdgesManager = EdgesManager(onEdgesChange, vertices)
    private val titleLabel: Label = Label("Edge List:")
    private val edgeTextArea: TextArea = TextArea()

    private var isUpdating = false

    init {
        spacing = 5.0
        padding = Insets(10.0)
        edgeTextArea.promptText = "Enter edges (one per line) in the format: A -> B (where A and B are vertex labels)"
        edgeTextArea.isWrapText = true
        setVgrow(edgeTextArea, Priority.ALWAYS)
        edgeTextArea.minHeight = 100.0

        edgeTextArea.textProperty().addListener { _, _, newText ->
            if (!isUpdating)
                handleTextChanged(newText)
        }

        children.addAll(titleLabel, edgeTextArea)
    }

    private fun handleTextChanged(newText: String) {
        edgesManager.handleTextChanged(newText)
        isUpdating = true
        onEdgesChange(edgesManager.getEnabledEdges())
        isUpdating = false
    }

    /**
     * Handles changes in the status of a vertex (enabled/disabled).
     * This updates the relevant edges by calling the `EdgesManager`'s method for vertex status change.
     */
    fun handleVertexStatusChange(vertexLabel: String, status: Boolean) =
        edgesManager.handleVertexStatusChange(vertexLabel, status)
}