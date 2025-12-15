import kotlin.random.Random
import kotlin.system.measureNanoTime

data class BenchmarkStats(
    val avgIterations: Double,
    val avgWCSS: Double,
    val avgMillis: Double
)

fun main() {
    val ks = listOf(3)                    // number of clusters
    val sizes = listOf(300, 900, 3000)    // total points per dataset
    val numTrials = 20                    // trials per configuration

    println("=== K-Means Benchmark (Gaussian synthetic data) ===")
    println("Trials per configuration: $numTrials\n")

    for (k in ks) {
        println("-------------- k = $k --------------")
        for (n in sizes) {
            val statsRandom = benchmarkInitStrategy(
                totalPoints = n,
                k = k,
                numTrials = numTrials,
                initStrategy = KMeans.InitStrategy.RANDOM
            )

            val statsPlus = benchmarkInitStrategy(
                totalPoints = n,
                k = k,
                numTrials = numTrials,
                initStrategy = KMeans.InitStrategy.KMEANS_PLUS_PLUS
            )

            println("Dataset size: $n points")
            printStats("RANDOM       ", statsRandom)
            printStats("K-MEANS++    ", statsPlus)
            println()
        }
        println()
    }
}

/**
 * Benchmark one initialization strategy on Gaussian points
 */
fun benchmarkInitStrategy(
    totalPoints: Int,
    k: Int,
    numTrials: Int,
    initStrategy: KMeans.InitStrategy
): BenchmarkStats {
    var sumIter = 0.0
    var sumWCSS = 0.0
    var sumMillis = 0.0

    // Equal cluster sizes, 3 blobs
    val clusterSizes = List(k) { totalPoints / k }
    val means = List(k) { idx ->
        when (idx) {
            0 -> doubleArrayOf(0.0, 0.0)
            1 -> doubleArrayOf(5.0, 5.0)
            else -> doubleArrayOf(-4.0, 4.0 + idx) // spread them a bit
        }
    }
    val stdDevs = List(k) { 0.7 }

    for (trial in 1..numTrials) {
        val data = generateGaussianClusters(clusterSizes, means, stdDevs)
        val rnd = Random(1000 + trial)

        lateinit var result: KMeansResult
        val elapsedNanos = measureNanoTime {
            result = KMeans.fit(
                data = data,
                k = k,
                random = rnd,
                initStrategy = initStrategy
            )
        }

        val wcss = computeWCSS(data, result.centroids, result.labels)

        sumIter += result.iterations
        sumWCSS += wcss
        sumMillis += elapsedNanos / 1_000_000.0
    }

    return BenchmarkStats(
        avgIterations = sumIter / numTrials,
        avgWCSS = sumWCSS / numTrials,
        avgMillis = sumMillis / numTrials
    )
}

/**
 * WCSS = sum over all points of squared distance to its assigned centroid.
 */
fun computeWCSS(
    data: List<Point>,
    centroids: List<Point>,
    labels: IntArray
): Double {
    var sum = 0.0
    for (i in data.indices) {
        val p = data[i]
        val c = centroids[labels[i]]
        sum += squaredDistance(p, c)
    }
    return sum
}

private fun squaredDistance(a: Point, b: Point): Double {
    var s = 0.0
    for (d in a.coords.indices) {
        val diff = a.coords[d] - b.coords[d]
        s += diff * diff
    }
    return s
}


private fun printStats(label: String, stats: BenchmarkStats) {
    println(
        "$label -> " +
                "avg iters = ${"%.2f".format(stats.avgIterations)}, " +
                "avg WCSS = ${"%.2f".format(stats.avgWCSS)}, " +
                "avg time = ${"%.3f".format(stats.avgMillis)} ms"
    )
}
