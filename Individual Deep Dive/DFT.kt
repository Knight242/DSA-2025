import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object DFT {
    fun calculate(input: List<Double>): List<Complex> {
        val n = input.size
        val output = MutableList(n) { Complex(0.0, 0.0) }

        for (k in 0 until n) {
            var sum = Complex(0.0, 0.0)
            for (t in 0 until n) {
                val angle = -2.0 * PI * t * k / n
                val exp = Complex(cos(angle), sin(angle))
                sum += exp * input[t]
            }
            output[k] = sum
        }
        return output
    }
}

data class Complex(val re: Double, val im: Double) {
    operator fun plus(other: Complex) = Complex(re + other.re, im + other.im)
    operator fun minus(other: Complex) = Complex(re - other.re, im - other.im)
    operator fun times(other: Complex) = Complex(re * other.re - im * other.im, re * other.im + im * other.re)
    operator fun times(scalar: Double) = Complex(re * scalar, im * scalar)
}
