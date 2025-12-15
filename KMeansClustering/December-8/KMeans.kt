import kotlin.math.sqrt
import kotlin.random.Random

/**
 * A data point in R^d represented by its coordinate vector
 *
 * Use [Point] as the for both input samples and centroids
 *
 * @property coords Coordinate array of length d
 */
data class Point(val coords: DoubleArray)

/**
 * Result of a K-means clustering
 *
 * @property centroids Final centroid locations (size = k)
 * @property labels Cluster for each input point
 * @property iterations Number of iterations completed until convergence or reaching max trials
 */
data class KMeansResult(
    val centroids: List<Point>,
    val labels: IntArray,
    val iterations: Int
)

/**
 * K-means clustering implementation with for RANDOM and K-MEANS++ initialization.
 *
 * K-means iteratively minimizes within-cluster sum of squares (WCSS) by alternating:
 *  1. assigning points to the nearest centroid, and
 *  2. recomputing each centroid as the mean of its assigned points
 *
 * RANDOM is fast but can converge to poor local minima; K-MEANS++
 * spreads initial centroids to improve stability and solution quality.
 */
object KMeans {

    /**
     * Different initializations for starting centroids
     */
    enum class InitStrategy {
        /** Choose k distinct data points uniformly at random as initial centroids. */
        RANDOM,

        /**
         * K-means++ initialization
         * - pick the first centroid uniformly at random,
         * - pick each centroid proportionate to squared distance
         *   to the closest centroid.
         */
        KMEANS_PLUS_PLUS
    }

    /**
     * Fits K-means to dataset
     *
     * @param data Input points to cluster. Must be non-empty and all points must share the same dimension.
     * @param k Number of clusters (centroids) to find. Must be true: 1 ≤ k ≤ data.size
     * @param maxIterations Hard cap on the number of K-means iterations
     * @param tolerance Convergence threshold based on total centroid movement between iterations
     * @param random Random number generator used for initialization and empty-cluster
     * @param initStrategy Initialization strategy for choosing the starting centroids.
     * @return [KMeansResult] contains the final centroids, labels, and iteration count
     *
     * @throws IllegalArgumentException if the input constraints are violated
     */
    fun fit(
        data: List<Point>,
        k: Int,
        maxIterations: Int = 300,
        tolerance: Double = 1e-4,
        random: Random = Random.Default,
        initStrategy: InitStrategy = InitStrategy.RANDOM
    ): KMeansResult {
        require(data.isNotEmpty()) { "Data must not be empty" }
        require(k > 0) { "k must be positive" }
        require(k <= data.size) { "k must be <= number of data points" }

        val dim = data[0].coords.size
        require(data.all { it.coords.size == dim }) { "All points must have same dimension" }

        // ---- Initialization: RANDOM vs K-MEANS++ ----
        var centroids = when (initStrategy) {
            InitStrategy.RANDOM -> initRandomCentroids(data, k, random)
            InitStrategy.KMEANS_PLUS_PLUS -> initKMeansPlusPlus(data, k, random)
        }

        val labels = IntArray(data.size)
        var iterations = 0

        while (iterations < maxIterations) {
            iterations++

            // Assign each point to the closest centroid
            for (i in data.indices) {
                labels[i] = closestCentroidIndex(data[i], centroids)
            }

            // Recompute centroids
            val newCentroids = recomputeCentroids(data, labels, k, dim, random)

            // Check for convergence
            val shift = totalCentroidShift(centroids, newCentroids)
            centroids = newCentroids

            if (shift < tolerance) break
        }

        return KMeansResult(centroids, labels, iterations)
    }

    /**
     * RANDOM initialization: picks k distinct data points uniformly at random as initial centroids.
     *
     * @param data Input dataset
     * @param k Number of centroids to sample
     * @param random RNG controlling the shuffle
     * @return List of k initial centroids (each is an existing data point)
     */
    private fun initRandomCentroids(
        data: List<Point>,
        k: Int,
        random: Random
    ): List<Point> {
        val indices = data.indices.shuffled(random).take(k)
        return indices.map { data[it] }
    }

    /**
     * K-means++ initialization.
     *
     * Process:
     *  1. Choose the first centroid uniformly at random from the data
     *  2. For each remaining centroid:
     *     - compute D(x)^2, the squared distance from x to its nearest chosen centroid,
     *     - sample the next centroid with probability proportional to D(x)^2.
     *
     * @param data Input dataset.
     * @param k Number of centroids to choose.
     * @param random RNG used for selection.
     * @return List of k initial centroids.
     */
    private fun initKMeansPlusPlus(
        data: List<Point>,
        k: Int,
        random: Random
    ): List<Point> {
        val centroids = mutableListOf<Point>()

        // 1) Choose first centroid uniformly at random
        centroids.add(data.random(random))

        // 2) Choose remaining centroids with probability ∝ D(x)^2
        while (centroids.size < k) {
            // Compute D(x)^2 = squared distance to nearest existing centroid
            val distances = DoubleArray(data.size) { i ->
                val p = data[i]
                var minDist = distanceSquared(p, centroids[0])
                for (c in 1 until centroids.size) {
                    val d = distanceSquared(p, centroids[c])
                    if (d < minDist) minDist = d
                }
                minDist
            }

            val total = distances.sum()

            // Edge case: if all distances are 0 (e.g., identical points), pick any remaining point
            if (total == 0.0) {
                val remaining = data.filter { it !in centroids }
                if (remaining.isNotEmpty()) {
                    centroids.add(remaining.random(random))
                } else {
                    break
                }
            } else {
                // Roulette-wheel selection proportional to D(x)^2
                val r = random.nextDouble(total)
                var cumulative = 0.0
                var chosenIndex = 0
                for (i in distances.indices) {
                    cumulative += distances[i]
                    if (r <= cumulative) {
                        chosenIndex = i
                        break
                    }
                }
                centroids.add(data[chosenIndex])
            }
        }

        return centroids
    }

    /**
     * Returns the index of the closest centroid to the given point using squared Euclidean distance.
     *
     * @param point Point to assign
     * @param centroids Current centroids
     * @return Index of the nearest centroid in [centroids]
     */
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

    /**
     * Computes squared Euclidean distance between two points.
     *
     * @param a First point
     * @param b Second point
     * @return ∑_j (a_j - b_j)^2
     */
    private fun distanceSquared(a: Point, b: Point): Double {
        var sum = 0.0
        for (i in a.coords.indices) {
            val diff = a.coords[i] - b.coords[i]
            sum += diff * diff
        }
        return sum
    }

    /**
     * Recomputes centroids as the mean of all points assigned to each cluster
     *
     * If a cluster becomes empty, a random data point is used as the new centroid for that cluster
     *
     * @param data input points
     * @param labels Cluster label for each point
     * @param k Number of clusters
     * @param dim Dimensionality of the points
     * @param random RNG used to re-seed empty clusters
     * @return Updated centroid list of size k
     */
    private fun recomputeCentroids(
        data: List<Point>,
        labels: IntArray,
        k: Int,
        dim: Int,
        random: Random
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
                // Empty cluster: reassign a random point as centroid
                centroids.add(data.random(random))
            } else {
                val mean = DoubleArray(dim) { d -> sums[cluster][d] / counts[cluster] }
                centroids.add(Point(mean))
            }
        }
        return centroids
    }

    /**
     * Computes the total centroid movement between two iterations
     *
     * if the total shift falls below [tolerance] in [fit], the code stops
     *
     * @param old Centroids from the previous iteration
     * @param new Centroids from the current iteration
     * @return Sum of Euclidean distances between corresponding centroids
     */
    private fun totalCentroidShift(old: List<Point>, new: List<Point>): Double {
        var sum = 0.0
        for (i in old.indices) {
            sum += sqrt(distanceSquared(old[i], new[i]))
        }
        return sum
    }
}
