import java.io.File
import kotlin.system.measureNanoTime
import kotlin.math.sin
import kotlin.math.PI

fun main() {
    val sizes = listOf(32, 64, 128, 256, 512, 1024, 2048)
    val outputFile = File("fft_benchmark.csv")

    outputFile.writeText("n,method,time_ms\n")

    for (n in sizes) {
        val input = List(n) { sin(2 * PI * it / n) }

        // DFT
        val dftTime = measureNanoTime {
            DFT.calculate(input)
        } / 1_000_000.0

        // FFT
        val fftInput = input.map { Complex(it, 0.0) }
        val fftTime = measureNanoTime {
            FFT.compute(fftInput)
        } / 1_000_000.0

        println("n=$n → DFT: $dftTime ms, FFT: $fftTime ms")

        outputFile.appendText("$n,DFT,$dftTime\n")
        outputFile.appendText("$n,FFT,$fftTime\n")
    }
}
