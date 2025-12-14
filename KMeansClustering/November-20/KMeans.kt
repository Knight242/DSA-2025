import kotlin.math.sqrt
import kotlin.random.Random

data class Point(val coords: DoubleArray)

data class KMeansResult(
    val centroids: List<Point>,
    val labels: IntArray,
    val iterations: Int
)

object KMeans {

    fun fit(
        data: List<Point>,
        k: Int,
        maxIterations: Int = 300,
        tolerance: Double = 1e-4,
        random: Random = Random.Default
    ): KMeansResult {

        val dim = data[0].coords.size
        require(data.all { it.coords.size == dim }) { "All points must have same dimension" }

        var centroids = makeRandomCentroids(data, k, random)

        val labels = IntArray(data.size)
        var iterations = 0

        while (iterations < maxIterations) {
            iterations++

            // Assign each point to the closest centroid
            for (i in data.indices) {
                labels[i] = closestCentroidIndex(data[i], centroids)
            }

            // Re calculate centroids
            val newCentroids = recomputeCentroids(data, labels, k, dim)

            // Check for convergence
            val shift = centroidShift(centroids, newCentroids)
            centroids = newCentroids

            if (shift < tolerance) break
        }
        return KMeansResult(centroids, labels, iterations)
    }

    private fun makeRandomCentroids(
        data: List<Point>,
        k: Int,
        random: Random
    ): List<Point> {
        val indices = data.indices.shuffled(random).take(k)
        return indices.map { data[it] }
    }

    private fun closestCentroidIndex(point: Point, centroids: List<Point>): Int {
        var bestIndex = 0
        var bestDist = distanceSquared(point, centroids[0])

        for (i in 1 until centroids.size) {
            val d = distanceSquared(point, centroids[i])
            if (d < bestDist) {
                bestDist = d
                bestIndex = i
            }
        }
        return bestIndex
    }

    private fun distanceSquared(a: Point, b: Point): Double {
        var sum = 0.0
        for (i in a.coords.indices) {
            val diff = a.coords[i] - b.coords[i]
            sum += diff * diff
        }
        return sum
    }

    private fun recomputeCentroids(
        data: List<Point>,
        labels: IntArray,
        k: Int,
        dim: Int
    ): List<Point> {
        val sums = Array(k) { DoubleArray(dim) }
        val counts = IntArray(k)

        for (i in data.indices) {
            val cluster = labels[i]
            counts[cluster]++
            val coords = data[i].coords
            for (d in 0 until dim) {
                sums[cluster][d] += coords[d]
            }
        }

        val centroids = mutableListOf<Point>()
        for (cluster in 0 until k) {
            if (counts[cluster] == 0) {
                centroids.add(data.random())
            } else {
                val mean = DoubleArray(dim) { d -> sums[cluster][d] / counts[cluster] }
                centroids.add(Point(mean))
            }
        }
        return centroids
    }

    private fun centroidShift(old: List<Point>, new: List<Point>): Double {
        var sum = 0.0
        for (i in old.indices) {
            sum += sqrt(distanceSquared(old[i], new[i]))
        }
        return sum
    }
}
