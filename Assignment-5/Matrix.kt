
/**
Square matrix, size n x n
Does addition, subtraction, multiplication
Performs Strassen's divide and conquer for multiplication
* @property size the number of rows/columns in the square matrix
*/

class Matrix(val size: Int) {

    // 2D array that stores matrix elements in row-col order
    private val data: Array<DoubleArray> = Array(size) { DoubleArray(size) }

    /*
    Sets a specific value in the matrix
     */
    fun set(row: Int, col: Int, value: Double) {
        data[row][col] = value
    }

    /*
    Gets the value stored at a given position in the matrix
     */
    fun get(row: Int, col: Int): Double {
        return data[row][col]
    }
    /**
     * Arithmetic Operations
     *
     * Adds this matrix to another matrix of the same size.
     * @param other the matrix to add
     * @return a new [Matrix] representing the sum of the two matrices
     */
    fun add(other: Matrix): Matrix {
        if (size != other.size) error("Matrix sizes must match")
        val result = Matrix(size)
        for (i in 0 until size)
            for (j in 0 until size)
                result.set(i, j, this.get(i, j) + other.get(i, j))
        return result
    }

    /**
     * Subtracts another matrix from this one.
     * @param other the matrix to subtract
     * @return a new [Matrix] representing (this - other)
     */

    fun subtract(other: Matrix): Matrix {
        if (size != other.size) error("Matrix sizes must match")
        val result = Matrix(size)
        for (i in 0 until size)
            for (j in 0 until size)
                result.set(i, j, this.get(i, j) - other.get(i, j))
        return result
    }

    /**
     * Multiplies this matrix by another matrix using the
     * O(n³) matrix multiplication algorithm.
     * @param other the matrix to multiply by
     * @return a new [Matrix] representing the product (this × other)
     */

    fun multiply(other: Matrix): Matrix {
        if (size != other.size) error("Matrix sizes must match")
        val result = Matrix(size)
        for (i in 0 until size)
            for (j in 0 until size)
                for (k in 0 until size)
                    result.set(i, j, result.get(i, j) + this.get(i, k) * other.get(k, j))
        return result
    }

    /**
     * Divides the matrix into four equally sized quadrants:
     * a11, a12, a21, a22 (each of size n/2 × n/2)
     * Used as part of Strassen’s recursive multiplication
     * @return a list containing [a11, a12, a21, a22]
     */
    private fun divide(): List<Matrix> {
        val half = size / 2
        val a11 = Matrix(half)
        val a12 = Matrix(half)
        val a21 = Matrix(half)
        val a22 = Matrix(half)
        for (i in 0 until half) {
            for (j in 0 until half) {
                a11.set(i, j, this.get(i, j))
                a12.set(i, j, this.get(i, j + half))
                a21.set(i, j, this.get(i + half, j))
                a22.set(i, j, this.get(i + half, j + half))
            }
        }
        return listOf(a11, a12, a21, a22)
    }
    /**
     * Combines four submatrices into a single matrix.
     * @param c11 top-left quadrant
     * @param c12 top-right quadrant
     * @param c21 bottom-left quadrant
     * @param c22 bottom-right quadrant
     * @return combined matrix of size (2 × c11.size)
     */
    private fun combine(c11: Matrix, c12: Matrix, c21: Matrix, c22: Matrix): Matrix {
        val half = c11.size
        val result = Matrix(half * 2)
        for (i in 0 until half) {
            for (j in 0 until half) {
                result.set(i, j, c11.get(i, j))
                result.set(i, j + half, c12.get(i, j))
                result.set(i + half, j, c21.get(i, j))
                result.set(i + half, j + half, c22.get(i, j))
            }
        }
        return result
    }

    /**
     * Multiplies this matrix by another matrix using
     * Strassen’s divide-and-conquer algorithm
     *
     * Reduces the number of recursive multiplications from 8 to 7
     * by combining addition and subtraction of submatrices.
     *
     * For small matrices (size ≤ [cutoff]), normal multiplication is used.
     *
     * @param other the matrix to multiply by
     * @param cutoff the threshold below which previous multiplication is used
     * @return resulting matrix after multiplication
     */
    fun strassenMultiply(other: Matrix, cutoff: Int = 64): Matrix {
        if (size != other.size) error("Matrix sizes must match")

        // Base or small case → normal multiply
        if (size <= cutoff) return this.multiply(other)

        val (a11, a12, a21, a22) = this.divide()
        val (b11, b12, b21, b22) = other.divide()

        val m1 = (a11.add(a22)).strassenMultiply(b11.add(b22), cutoff)
        val m2 = (a21.add(a22)).strassenMultiply(b11, cutoff)
        val m3 = a11.strassenMultiply(b12.subtract(b22), cutoff)
        val m4 = a22.strassenMultiply(b21.subtract(b11), cutoff)
        val m5 = (a11.add(a12)).strassenMultiply(b22, cutoff)
        val m6 = (a21.subtract(a11)).strassenMultiply(b11.add(b12), cutoff)
        val m7 = (a12.subtract(a22)).strassenMultiply(b21.add(b22), cutoff)

        val c11 = m1.add(m4).subtract(m5).add(m7)
        val c12 = m3.add(m5)
        val c21 = m2.add(m4)
        val c22 = m1.subtract(m2).add(m3).add(m6)

        return combine(c11, c12, c21, c22)
    }

    fun printMatrix() {
        for (i in 0 until size) {
            for (j in 0 until size) {
                print("${data[i][j]}\t")
            }
            println()
        }
    }
}

fun main() {
    val A = Matrix(2)
    val B = Matrix(2)

    A.set(0, 0, 1.0); A.set(0, 1, 2.0)
    A.set(1, 0, 3.0); A.set(1, 1, 4.0)

    B.set(0, 0, 5.0); B.set(0, 1, 6.0)
    B.set(1, 0, 7.0); B.set(1, 1, 8.0)

    println("Normal multiplication:")
    A.multiply(B).printMatrix()

    println("\nStrassen multiplication:")
    A.strassenMultiply(B, cutoff = 1).printMatrix()
}
