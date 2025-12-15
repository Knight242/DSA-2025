import java.io.File

fun loadSpotifyEnergyValence(filePath: String): List<Point> {
    val lines = File(filePath).readLines().filter { it.isNotBlank() }


    val header = lines.first().split(',')
    println("Header columns: ${header.joinToString(" | ")}")

    val energyIdx = header.indexOfFirst { it.trim().equals("energy", ignoreCase = true) }
    val valenceIdx = header.indexOfFirst { it.trim().equals("valence", ignoreCase = true) }

    val dataLines = lines.drop(1)
    val points = mutableListOf<Point>()

    for (line in dataLines) {
        val parts = line.split(',')

        if (parts.size <= maxOf(energyIdx, valenceIdx)) continue

        val energy = parts[energyIdx].trim().toDoubleOrNull()
        val valence = parts[valenceIdx].trim().toDoubleOrNull()

        if (energy != null && valence != null &&
            energy in 0.0..1.0 && valence in 0.0..1.0) {

            points.add(Point(doubleArrayOf(energy, valence)))
        }
    }

    println("Loaded ${points.size} valid songs")
    return points
}
