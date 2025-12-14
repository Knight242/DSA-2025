fun main() {
    val data = generateGaussianClusters(
        clusterSizes = listOf(40, 40, 40),
        means = listOf(
            doubleArrayOf(0.0, 0.0),
            doubleArrayOf(5.0, 5.0),
            doubleArrayOf(-4.0, 4.0)
        ),
        stdDevs = listOf(
            0.5,
            0.6,
            0.7
        )
    )
    val k = 3
    println("Gaussian Points (${data.size} points) ")
    runKMeansExperiment(data, k)
}

fun runKMeansExperiment(data: List<Point>, k: Int) {
    val result = KMeans.fit(data, k)

    println("Centroids:")
    result.centroids.forEachIndexed { idx, c ->
        println("  Cluster $idx: ${
            c.coords.joinToString(prefix = "(", postfix = ")")
        }")
    }

    println("Assigned Points:")
    result.labels.forEachIndexed { i, label ->
        val p = data[i]
        println("  Point $i (${p.coords.joinToString()}) -> Cluster $label")
    }
    showClusterPlot(data, result.labels, result.centroids)
}
