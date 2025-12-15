import kotlin.random.Random

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
    val numTrials = 30

    println("Dataset size: ${data.size} points, k = $k")


    val resultRandom = KMeans.fit(
        data = data,
        k = k,
        random = Random(123),
        initStrategy = KMeans.InitStrategy.RANDOM
    )

    val resultPlus = KMeans.fit(
        data = data,
        k = k,
        random = Random(456),
        initStrategy = KMeans.InitStrategy.KMEANS_PLUS_PLUS
    )


    showSideBySideClusterPlots(
        data = data,
        labelsRandom = resultRandom.labels,
        centroidsRandom = resultRandom.centroids,
        labelsPlus = resultPlus.labels,
        centroidsPlus = resultPlus.centroids
    )
}


