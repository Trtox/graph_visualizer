package trtox.graphvisualizer.logic

import trtox.graphvisualizer.ui.VerticesUI

/**
 * Manages the edges of the graph and updates them based on changes in the edge text or the status of vertices.
 * It interacts with the [VerticesUI] class to ensure all vertices are valid when creating edges and updates the graph
 * accordingly. It also triggers the [onEdgesChange] callback whenever the set of enabled edges changes.
 *
 * The class is responsible for adding, removing, and updating edges. It ensures that any vertices involved in an edge
 * are valid and updated. The edges are stored in an internal [Edges] object.
 */
class EdgesManager(
    val onEdgesChange: (Set<Edge>) -> Unit,
    private val vertices: VerticesUI
) {
    private val edges: Edges = Edges()

    /**
     * Adds a new edge to the graph based on the given edge string representation.
     * If the edge is valid, it creates or updates the involved vertices and adds the edge to the graph.
     * The edge should be represented as a string in the format "A -> B", where A and B are vertex labels.
     */
    private fun addEdge(edge: String) {
        if (isValidEdge(edge)) {
            val parts = edge.split("->").map { it.trim() }

            var leftVertex = vertices.getVertexByLabel(parts[0])
            if (leftVertex == null) {
                leftVertex = Vertex(vertices.getVertexCount() + 1, parts[0])
                vertices.addVertex(leftVertex)
            }

            var rightVertex = vertices.getVertexByLabel(parts[1])
            if (rightVertex == null) {
                rightVertex = Vertex(vertices.getVertexCount() + 1, parts[1])
                vertices.addVertex(rightVertex)
            }
            edges.addEdge(Edge(leftVertex, rightVertex))
        }
    }

    /**
     * Updates the graph according to the new edges represented by [newText].
     * This method removes all existing edges and adds the new ones while ensuring vertices are updated or removed.
     * The edge list in [newText] should follow the format "A -> B", with each edge on a new line.
     */
    fun handleTextChanged(newText: String) {
        val newEdges = newText.lines().map { it.trim() }.filter { it.isNotEmpty() }

        val vertexStates = vertices.getAllVertices().associate { it.label to it.isEnabled }

        val referencedVertices = mutableSetOf<String>()
        newEdges.forEach { edge ->
            if (isValidEdge(edge)) {
                val parts = edge.split("->").map { it.trim() }
                referencedVertices.add(parts[0])
                referencedVertices.add(parts[1])
            }
        }

        edges.removeAllEdges()

        newEdges.forEach { edge ->
            if (isValidEdge(edge))
                addEdge(edge)
        }

        referencedVertices.forEach { vertexLabel ->
            var vertex = vertices.getVertexByLabel(vertexLabel)

            if (vertex == null) {
                vertex = Vertex(vertices.getVertexCount() + 1, vertexLabel)
                vertices.addVertex(vertex)
            }

            vertexStates[vertexLabel]?.let { vertices.setVertexLabelState(vertexLabel, it) }
        }

        val verticesToRemove = vertices.getAllVertices().filter { it.label !in referencedVertices }
        verticesToRemove.forEach { vertices.removeVertexByLabel(it.label) }

        vertices.getAllVertices().forEach { vertex ->
            vertexStates[vertex.label]?.let { state ->
                vertices.setVertexLabelState(vertex.label, state)
            }
        }
    }

    /**
     * Updates the edges containing a vertex with [vertexLabel] based on the [status].
     * This method is called when the vertex status changes (enabled or disabled).
     */
    fun handleVertexStatusChange(vertexLabel: String, status: Boolean) {
        edges.updateEdgesForVertex(vertexLabel, status)
        onEdgesChange(edges.getEnabledEdges())
    }

    /**
     * Returns the set of enabled edges in the graph.
     */
    fun getEnabledEdges(): Set<Edge> = edges.getEnabledEdges()

    // Validates if the edge format is correct.
    private fun isValidEdge(edge: String): Boolean {
        val parts = edge.split("->").map { it.trim() }
        return parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()
    }
}