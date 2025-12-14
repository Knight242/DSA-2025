import kotlin.math.ln
import kotlin.math.sqrt
import kotlin.random.Random

fun generateGaussianClusters(
    clusterSizes: List<Int>,
    means: List<DoubleArray>,
    stdDevs: List<Double>
): List<Point> {
    val points = mutableListOf<Point>()
    val rnd = Random(42)

    for (i in clusterSizes.indices) {
        val n = clusterSizes[i]
        val mean = means[i]
        val sd = stdDevs[i]

        repeat(n) {
            val x = rnd.nextGaussian() * sd + mean[0]
            val y = rnd.nextGaussian() * sd + mean[1]
            points.add(Point(doubleArrayOf(x, y)))
        }
    }

    return points
}

fun Random.nextGaussian(): Double {
    var u: Double
    var v: Double
    var s: Double

    do {
        u = nextDouble(-1.0, 1.0)
        v = nextDouble(-1.0, 1.0)
        s = u * u + v * v
    } while (s >= 1 || s == 0.0)

    // Box–Muller transform using natural log
    val multiplier = sqrt(-2.0 * ln(s) / s)
    return u * multiplier
}
