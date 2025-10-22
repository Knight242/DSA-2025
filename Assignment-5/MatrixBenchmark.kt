import kotlin.system.measureNanoTime
import kotlin.random.Random

fun main() {
    val sizes = listOf(32, 64, 128, 256)
    val cutoffs = listOf(8, 16, 32, 64, 128)
    val trials = 3

    println("n,method,cutoff,time_ms")

    for (n in sizes) {
        val a = randomMatrix(n)
        val b = randomMatrix(n)

        // Normal multiplication timing
        val tNormal = (1..trials).map {
            measureNanoTime { a.multiply(b) } / 1e6
        }.average()
        println("$n,normal,-,${"%.2f".format(tNormal)}")

        // Strassen timing with different cutoffs
        for (cut in cutoffs) {
            val tStrassen = (1..trials).map {
                measureNanoTime { a.strassenMultiply(b, cutoff = cut) } / 1e6
            }.average()
            println("$n,strassen,$cut,${"%.2f".format(tStrassen)}")
        }
    }
}
