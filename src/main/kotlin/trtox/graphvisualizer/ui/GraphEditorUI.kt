package trtox.graphvisualizer.ui

import javafx.geometry.Insets
import javafx.scene.control.SplitPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import trtox.graphvisualizer.logic.Edge

/**
 * Represents the user interface for editing a graph.
 *
 * This class manages the layout and interactions for the graph editor. It contains two main areas:
 * 1. An area for managing and displaying the edges (`EdgesUI`).
 * 2. An area for managing and displaying the vertices (`VerticesUI`).
 *
 * The graph editor listens for changes in the edges and updates the UI accordingly. When a vertex's
 * status changes, the edges UI is notified to reflect the changes.
 *
 * The layout is arranged in a vertical split pane, where the upper section displays the edges, and the
 * lower section displays the vertices. The split positions are set to allow for a flexible, resizable layout.
 *
 * The class also configures basic UI properties such as spacing, padding, and growth behavior.
 */
class GraphEditorUI(
    onEdgesChange: (Set<Edge>) -> Unit
) : VBox() {

    private lateinit var edgesUI: EdgesUI

    init {
        val verticesUI = VerticesUI { vertexLabel, status ->
            edgesUI.handleVertexStatusChange(vertexLabel, status)
        }

        edgesUI = EdgesUI(onEdgesChange, verticesUI)
        spacing = 10.0
        padding = Insets(10.0)

        val splitPane = SplitPane()
        splitPane.orientation = javafx.geometry.Orientation.VERTICAL
        splitPane.items.addAll(edgesUI, verticesUI)

        splitPane.setDividerPositions(0.3, 0.7)

        children.addAll(splitPane)
        setVgrow(splitPane, Priority.ALWAYS)
    }
}