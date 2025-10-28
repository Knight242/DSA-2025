import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object FFT {
    fun compute(input: List<Complex>): List<Complex> {
        val n = input.size
        if (n == 1) return input

        // Check that input size is a power of two
        if (n % 2 != 0) throw IllegalArgumentException("Input size must be a power of two")

        val even = compute(input.filterIndexed { index, _ -> index % 2 == 0 })
        val odd = compute(input.filterIndexed { index, _ -> index % 2 == 1 })

        val result = MutableList(n) { Complex(0.0, 0.0) }
        for (k in 0 until n / 2) {
            val angle = -2.0 * PI * k / n
            val wk = Complex(cos(angle), sin(angle))
            result[k] = even[k] + wk * odd[k]
            result[k + n / 2] = even[k] - wk * odd[k]
        }
        return result
    }
}
