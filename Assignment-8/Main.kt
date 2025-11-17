import KDTree.*
import kotlin.time.Duration
import kotlin.time.measureTime
import kotlin.random.Random

// Brute Force Nearest Neighbor

/**
 * Performs brute-force nearest-neighbor search by checking all points
 *
 * Calculates the Euclidean distance between the query point and
 * every point in the dataset, selecting the one with minimum distance
 *
 * @param points the list of k-dimensional points to search through
 * @param query the k-dimensional query point
 * @return the point in [points] that is closest to [query]
 */
fun bruteForceNearest(points: List<Point>, query: Point): Point {
    return points.minBy { euclideanDistance(it, query) }
}

/**
 * Creates a k-dimensional point where the coordinates are random doubles
 * in the range [0.0, 100.0].
 *
 * @param k the number of dimensions
 * @return a new random k-dimensional point
 */
fun randomPoint(k: Int): Point {
    val arr = DoubleArray(k)
    for (i in 0 until k) {
        arr[i] = Random.nextDouble(0.0, 100.0)
    }
    return arr
}

/**
 * Create a list of N randomly generated k-dimensional points
 *
 * @param k the dimensionality of each point
 * @param n the number of points to generate
 * @return a list of randomly generated k-dimensional points
 */
fun generatePoints(k: Int, n: Int): List<Point> {
    return List(n) { randomPoint(k) }
}

/**
 * Full experiment comparing KD-tree nearest-neighbor and brute-force
 * nearest-neighbor performance for given dimensionality and dataset size.
 *
 * 1. Generates `numPoints` training points
 * 2. Generates 1000 random test queries
 * 3. Measures:
 * - KD-tree build time
 * - KD-tree query time for 1000 queries
 * - Brute-force query time for 1000 queries
 *
 * @param k the dimensionality of each point
 * @param numPoints the number of points in the training dataset
 *
 * @return a [Triple] of:
 * - build time for KD-tree construction
 * - total time to handle 1000 KD-tree nearest-neighbor queries
 * - total time to handle 1000 brute-force nearest-neighbor queries
 */
fun runExperiment(k: Int, numPoints: Int): Triple<Duration, Duration, Duration> {

    // 1. Generate training & testing data
    val trainingPoints = generatePoints(k, numPoints)
    val testPoints = generatePoints(k, 1000)

    // 2. Build KD-tree
    lateinit var kdTree: KDTree
    val buildTime = measureTime {
        kdTree = KDTree(trainingPoints)
    }

    // 3. KD-tree queries
    val kdQueryTime = measureTime {
        for (q in testPoints) {
            nearestNeighbor(kdTree.root, q)
        }
    }

    // 4. Brute-force queries
    val bruteTime = measureTime {
        for (q in testPoints) {
            bruteForceNearest(trainingPoints, q)
        }
    }

    return Triple(buildTime, kdQueryTime, bruteTime)
}

// Main function to run KD-tree vs brute-force benchmarking
fun main() {
    val kValues = listOf(2, 5, 10)
    val nValues = listOf(10, 100, 1000, 10000)

    // Print header
    println(String.format("%-5s %-10s %-15s %-15s %-15s",
        "k", "N", "Build(ms)", "KDQuery(ms)", "Brute(ms)"
    ))
    println("---------------------------------------------------------------------")

    for (k in kValues) {
        for (n in nValues) {
            val (build, kdQuery, brute) = runExperiment(k, n)

            println(String.format(
                "%-5d %-10d %-15d %-15d %-15d",
                k,
                n,
                build.inWholeMilliseconds,
                kdQuery.inWholeMilliseconds,
                brute.inWholeMilliseconds
            ))
        }
    }
}


