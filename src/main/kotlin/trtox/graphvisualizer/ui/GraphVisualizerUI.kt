package trtox.graphvisualizer.ui

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.control.ScrollPane
import java.io.File
import java.io.IOException
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import trtox.graphvisualizer.logic.Edge
import kotlin.math.sqrt
import kotlin.math.max

/**
 * User interface component responsible for visualizing and displaying a graph diagram.
 *
 * This class generates a visual representation of a graph based on a set of edges, using the Mermaid diagramming tool.
 * It manages the process of generating the diagram in a separate thread and updating the UI once the diagram is ready.
 * The diagram is displayed as an image within a scrollable pane, and the class handles user interaction like panning.
 *
 * The class also includes debouncing functionality to prevent excessive updates while changes to the graph edges are made.
 *
 * It works as follows:
 * 1. The `displayDiagram` method is called when the graph edges change.
 * 2. The method generates a Mermaid diagram, saves it to a temporary file, and uses an external process to generate the diagram image.
 * 3. Once the diagram is generated, it is displayed as an image inside the scrollable pane.
 * 4. Error handling is included in case the diagram generation fails.
 *
 * The diagram generation process uses the `mmdc` (Mermaid CLI) command-line tool, which must be installed on the system.
 */
class GraphVisualizerUI : ScrollPane() {

    private var currentTask: javafx.concurrent.Task<Boolean>? = null
    private var currentProcess: Process? = null
    private var debounceTimer: javafx.animation.PauseTransition? = null

    init {
        this.fitToWidthProperty().set(true)
        this.fitToHeightProperty().set(true)
    }

    /**
     * Displays the graph diagram based on the provided set of edges.
     *
     * This method generates the Mermaid diagram, spawns a background thread to process it, and updates the UI with the resulting image.
     * It employs debouncing to avoid triggering the diagram generation too frequently when edges are updated.
     */
    fun displayDiagram(edges: Set<Edge>) {
        debounceTimer?.stop()
        debounceTimer = javafx.animation.PauseTransition(javafx.util.Duration.millis(300.0)).apply {
            setOnFinished {
                runDisplayDiagram(edges)
            }
            play()
        }
    }

    private fun runDisplayDiagram(edges: Set<Edge>) {
        currentTask?.let {
            if (it.isRunning) {
                it.cancel()
            }
        }

        val mermaidCode = getMermaidCode(edges)

        val inputFile = File.createTempFile("graph", ".mmd")
        inputFile.writeText(mermaidCode)

        val outputFile = File.createTempFile("diagram", ".png")

        val task = object : javafx.concurrent.Task<Boolean>() {
            override fun call(): Boolean {
                if (isCancelled) return false
                return generateDiagram(inputFile, outputFile)
            }
        }

        task.setOnSucceeded {
            (this.content as? ImageView)?.image?.cancel()
            this.content = null

            if (task.value) {
                val image = Image(outputFile.toURI().toString())
                val imageView = ImageView(image)

                imageView.fitWidth = image.width
                imageView.fitHeight = image.height
                imageView.isPreserveRatio = true

                javafx.application.Platform.runLater {
                    this.content = imageView
                    this.isPannable = true
                }

                outputFile.delete()
                inputFile.delete()
            }
        }

        task.setOnFailed {
            showErrorAlert("Failed to generate diagram.")
        }

        task.setOnCancelled {
            currentProcess?.destroyForcibly()
            currentProcess = null
        }

        currentTask = task
        Thread(task).start()
    }

    private fun generateDiagram(inputFile: File, outputFile: File): Boolean {
        val inputText = inputFile.readText()
        val edgeCount = inputText.lines().count { it.contains("-->") }
        val scaleFactor = max(1.0, sqrt(edgeCount.toDouble() / 50))
        val width = (4000 * scaleFactor).toInt()
        val height = (4000 * scaleFactor).toInt()
        currentProcess?.destroyForcibly()
        val command = listOf(
            "mmdc",
            "-i", inputFile.absolutePath,
            "-o", outputFile.absolutePath,
            "-b", "light",
            "-w", "$width",
            "-H", "$height"
        )
        return try {
            currentProcess = ProcessBuilder(command).start()
            val exitCode = currentProcess!!.waitFor()
            currentProcess = null
            exitCode == 0
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun getMermaidCode(edges: Set<Edge>): String {
        val mermaidHeader = "graph TD"
        val edgesCode = edges.joinToString("\n  ") { "${it.leftVertex.label} --> ${it.rightVertex.label}" }

        return """
        $mermaidHeader
        $edgesCode
    """.trimIndent()
    }

    private fun showErrorAlert(message: String) {
        val alert = Alert(AlertType.ERROR)
        alert.title = "Error"
        alert.headerText = null
        alert.contentText = message
        alert.showAndWait()
    }
}