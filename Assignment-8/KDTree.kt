import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Represents a k-dimensional point stored as a DoubleArray
 *
 * Each index corresponds to a coordinate in k-dimensional space
 *
 * Example for k = 3:
 * val p: Point = doubleArrayOf(2.0, 5.0, 1.3)
 */
typealias Point = DoubleArray

/**
 * A node within a k-d tree
 *
 * The KD node stores:
 * A k-dimensional point ([point])
 * The axis index ([axis]) used to split the space at this node
 * Optional left ([left]) and right ([right]) child nodes
 *
 * @property point the k-dimensional coordinate stored at the node
 * @property axis the splitting dimension used at this node (depth % k)
 * @property left the subtree containing points "less than" this point along [axis]
 * @property right the subtree containing points "greater than" this point along [axis]
 */
data class KDNode(
    val point: Point,
    val axis: Int,
    val left: KDNode? = null,
    val right: KDNode? = null
)

/**
 * Calculates the Euclidean distance between two k-dimensional points
 *
 * @param a the first point
 * @param b the second point
 * @return the Euclidean distance between [a] and [b]
 */
fun euclideanDistance(a: Point, b: Point): Double {
    var sum = 0.0
    for (i in a.indices) {
        val diff = a[i] - b[i]
        sum += diff * diff
    }
    return sqrt(sum)
}

/**
 * The k-d tree with nearest-neighbor search in k-dimensional space
 *
 * Tree properties:
 *  Space is recursively partitioned along different coordinate axes
 *  At depth d, the splitting axis is (d % k)
 *
 * @property root the root node of the k-d tree
 */
class KDTree(points: List<Point>) {

    // Root node of the k-d tree
    val root: KDNode? = buildKDTree(points)
    /**
     * Recursively builds a k-d tree from a list of points
     *
     * Steps:
     * 1. Select the splitting axis using depth % k
     * 2. Sort points along that axis
     * 3. Choose the median as the node's point
     * 4. Recursively construct left and right subtrees
     *
     * @param points list of k-dimensional points to build from
     * @param depth current recursion depth (determines splitting axis)
     * @return a KDNode representing the subtree root, or null if empty
     */
    private fun buildKDTree(points: List<Point>, depth: Int = 0): KDNode? {
        if (points.isEmpty()) return null

        val k = points[0].size
        val axis = depth % k

        // Sort by current dimension and pick median
        val sorted = points.sortedBy { it[axis] }
        val median = sorted.size / 2

        return KDNode(
            point = sorted[median],
            axis = axis,
            left = buildKDTree(sorted.subList(0, median), depth + 1),
            right = buildKDTree(sorted.subList(median + 1, sorted.size), depth + 1)
        )
    }
}

/**
 * Result of Nearest-neighbor search
 *
 * @property point the closest point found so far (may be null)
 * @property dist the distance to the closest found point
 */
data class NNResult(val point: Point?, val dist: Double)

/**
 * Nearest-neighbor search in the k-d tree
 *
 * 1. Recurse down to the selected subtree based on the splitting axis
 * 2. Update the best so far candidate
 * 3. Check whether we might find a closer point on the other side
 * - If so, search the other subtree
 *
 * @param node the current KDNode being explored
 * @param query the query point to compare against
 * @param best the best result found so far (distance + point)
 *
 * @return an [NNResult] containing the nearest point and its distance
 */
fun nearestNeighbor(
    node: KDNode?,
    query: Point,
    best: NNResult = NNResult(null, Double.POSITIVE_INFINITY)
): NNResult {
    if (node == null) return best

    val axis = node.axis
    val currentDist = euclideanDistance(query, node.point)

    // Update best match so far
    var bestResult =
        if (currentDist < best.dist) NNResult(node.point, currentDist) else best

    // Determine which subtree to explore first
    val goLeft = query[axis] < node.point[axis]
    val first = if (goLeft) node.left else node.right
    val second = if (goLeft) node.right else node.left

    // Explore the primary subtree
    bestResult = nearestNeighbor(first, query, bestResult)

    // Check if the splitting plane could contain a closer point
    val distanceToPlane = abs(query[axis] - node.point[axis])
    if (distanceToPlane < bestResult.dist) {
        bestResult = nearestNeighbor(second, query, bestResult)
    }

    return bestResult
}
