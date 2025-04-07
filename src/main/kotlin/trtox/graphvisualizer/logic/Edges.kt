package trtox.graphvisualizer.logic

class Edges {
    private val edgeSet: MutableSet<Edge> = mutableSetOf()

    fun addEdge(edge: Edge) = edgeSet.add(edge)
    fun removeAllEdges() = edgeSet.clear()

    fun contains(edge: Edge) = edge in edgeSet
    fun getEnabledEdges(): Set<Edge> = edgeSet.filter { it.isEnabled }.toSet()

    fun updateEdgesForVertex(vertexLabel: String, isEnabled: Boolean) {
        edgeSet.forEach { edge ->
            if (edge.leftVertex.label == vertexLabel)
                edge.leftVertex.isEnabled = isEnabled

            if (edge.rightVertex.label == vertexLabel)
                edge.rightVertex.isEnabled = isEnabled

            edge.isEnabled = edge.leftVertex.isEnabled && edge.rightVertex.isEnabled
        }
    }

    override fun toString(): String = edgeSet.joinToString("->") { it.toString() }

}

data class Edge(val leftVertex: Vertex, val rightVertex: Vertex) {
    var isEnabled: Boolean = leftVertex.isEnabled && rightVertex.isEnabled

    override fun toString(): String = leftVertex.label + "->" + rightVertex.label
}
