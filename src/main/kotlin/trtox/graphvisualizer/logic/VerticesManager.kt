package trtox.graphvisualizer.logic

/**
 * Manages the collection of vertices in the graph.
 *
 * Provides functionality to add, remove, and update vertices, as well as retrieve information about the
 * vertices such as their state and count. Vertices are stored in a collection, and operations are performed
 * based on the vertex labels.
 */
class VerticesManager {

    private val vertices: Vertices = Vertices()
    private var vertexCount: Int = 0

    /**
     * Adds the given [vertex] to the collection of vertices.
     * If a vertex with the same label already exists, its degree is incremented.
     * Returns true if the vertex was successfully added or its degree was incremented,
     * false if the vertex is disabled and cannot be added.
     */
    fun addVertex(vertex: Vertex): Boolean {
        if (!vertex.isEnabled) return false
        vertices.getAllVertices().find { it.label == vertex.label }?.let {
            ++it.degree
            return true
        }

        vertices.addVertex(vertex)
        updateVertexCount()
        return true
    }

    /**
     * Removes the vertex with the given [label] from the collection.
     * Returns true if the vertex was found and removed,
     * false if no vertex with the given label was found.
     */
    fun removeVertexByLabel(label: String): Boolean {
        val vertexToRemove = vertices.getAllVertices().find { it.label == label } ?: return false
        vertices.removeVertex(vertexToRemove) // Remove the vertex from the collection
        updateVertexCount() // Update the vertex count
        return true
    }

    /**
     * Sets the state of the vertex with the given [label] to [newState].
     * Returns true if the state was successfully updated,
     * false if no vertex with the given label was found.
     */
    fun setVertexState(label: String, newState: Boolean): Boolean {
        val vertex = vertices.getAllVertices().find { it.label == label } ?: return false
        vertex.isEnabled = newState // Update the state of the vertex
        return true
    }

    /**
     * Returns the vertex with the given [label], or null if no such vertex exists.
     */
    fun getVertexByLabel(label: String): Vertex? = vertices.getVertexByLabel(label)

    /**
     * Returns all vertices in the collection as a set.
     */
    fun getAllVertices(): Set<Vertex> = vertices.getAllVertices()

    private fun updateVertexCount() {
        vertexCount = vertices.size()
    }

    /**
     * Returns the number of vertices currently present in the collection.
     */
    fun getVertexCount(): Int = vertices.size()
}