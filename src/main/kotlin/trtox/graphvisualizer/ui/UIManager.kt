package trtox.graphvisualizer.ui

import javafx.geometry.Rectangle2D
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.scene.control.SplitPane
import javafx.stage.Screen

/**
 * Manages the user interface for the graph visualizer and editor.
 *
 * This class is responsible for setting up and organizing the layout of the
 * application window, which includes two main areas:
 * 1. A graph input area, which allows users to edit the graph.
 * 2. A graph display area, which visualizes the graph.
 *
 * It initializes the `GraphVisualizerUI` and `GraphEditorUI` components,
 * arranges them in a split pane, and configures the application window.
 *
 * The class takes a [stage] object, which is used to display the user interface
 * for the application.
 */
class UIManager(
    private val stage: Stage
) {
    private val graphVisualizerUI = GraphVisualizerUI()
    private val graphEditorUI = GraphEditorUI { edges ->
        graphVisualizerUI.displayDiagram(edges)
    }

    /**
     * Initializes the `GraphVisualizerUI` and `GraphEditorUI` components.
     * Divides window in areas for displaying and editing graph.
     */
    fun setupUI() {
        val (graphInputArea, graphDisplayArea) = Pair(graphEditorUI, graphVisualizerUI)

        val splitPane = SplitPane(graphInputArea, graphDisplayArea)
        splitPane.setDividerPositions(0.25)

        val screenBounds: Rectangle2D = Screen.getPrimary().visualBounds
        stage.title = "Graph Visualizer"
        stage.scene = Scene(splitPane, screenBounds.width, screenBounds.height)
        stage.show()
    }
}