package trtox.graphvisualizer.logic

class Vertices {
    private val vertexSet: MutableSet<Vertex> = mutableSetOf()

    fun addVertex(vertex: Vertex) = vertexSet.add(vertex)
    fun removeVertex(vertex: Vertex) = vertexSet.remove(vertex)

    fun contains(vertex: Vertex) = vertex in vertexSet
    fun getVertexByLabel(label: String): Vertex? = vertexSet.find { it.label == label }
    fun getAllVertices(): Set<Vertex> = vertexSet.toSet()

    fun size() = vertexSet.size
}

data class Vertex(val id: Int, var label: String) {
    var isEnabled: Boolean = true
    var degree: Int = 1
}
