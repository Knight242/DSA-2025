import kotlin.random.Random

fun main() {
    val data = loadSpotifyEnergyValence("src/data/raw_songs_dataset.csv")

    println("Loaded ${data.size} songs with features (energy, valence)")

    val k = 4

    val result = KMeans.fit(
        data = data,
        k = k,
        random = Random(42),
        initStrategy = KMeans.InitStrategy.KMEANS_PLUS_PLUS
    )

    println("K-means++ on Spotify songs:")
    println("  k = $k")
    println("  iterations to convergence = ${result.iterations}")

    showClusterPlot(
        data = data,
        labels = result.labels,
        centroids = result.centroids,
        title = "Spotify Songs Clustering (Energy vs Valence)",
        xLabel = "Energy",
        yLabel = "Valence",
        clusterNames = listOf(
            "Chill / Low Energy",
            "Dark High Energy",
            "Happy Upbeat",
            "Mellow Positive",
            "Other / Mixed"
        )
    )
}
