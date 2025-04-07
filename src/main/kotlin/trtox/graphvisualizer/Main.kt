package trtox.graphvisualizer

import javafx.application.Application
import javafx.stage.Stage
import trtox.graphvisualizer.ui.UIManager

class GraphVisualizer : Application() {
    override fun start(stage: Stage) {
        val uiManager = UIManager(stage)
        uiManager.setupUI()
    }
}

fun main() {
    Application.launch(GraphVisualizer::class.java)
}