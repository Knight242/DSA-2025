import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MatrixTest {

    @Test
    fun testSetAndGet() {
        val m = Matrix(2)
        m.set(0, 0, 3.14)
        assertEquals(3.14, m.get(0, 0), "Matrix set/get should return the correct value")
    }

    @Test
    fun testAddition() {
        val A = Matrix(2)
        val B = Matrix(2)
        A.set(0, 0, 1.0); A.set(0, 1, 2.0)
        A.set(1, 0, 3.0); A.set(1, 1, 4.0)
        B.set(0, 0, 5.0); B.set(0, 1, 6.0)
        B.set(1, 0, 7.0); B.set(1, 1, 8.0)

        val C = A.add(B)
        assertEquals(6.0, C.get(0, 0))
        assertEquals(8.0, C.get(0, 1))
        assertEquals(10.0, C.get(1, 0))
        assertEquals(12.0, C.get(1, 1))
    }

    @Test
    fun testSubtraction() {
        val A = Matrix(2)
        val B = Matrix(2)
        A.set(0, 0, 5.0); A.set(0, 1, 7.0)
        A.set(1, 0, 9.0); A.set(1, 1, 11.0)
        B.set(0, 0, 1.0); B.set(0, 1, 2.0)
        B.set(1, 0, 3.0); B.set(1, 1, 4.0)

        val C = A.subtract(B)
        assertEquals(4.0, C.get(0, 0))
        assertEquals(5.0, C.get(0, 1))
        assertEquals(6.0, C.get(1, 0))
        assertEquals(7.0, C.get(1, 1))
    }

    @Test
    fun testStandardMultiplication() {
        val A = Matrix(2)
        val B = Matrix(2)
        A.set(0, 0, 1.0); A.set(0, 1, 2.0)
        A.set(1, 0, 3.0); A.set(1, 1, 4.0)
        B.set(0, 0, 5.0); B.set(0, 1, 6.0)
        B.set(1, 0, 7.0); B.set(1, 1, 8.0)

        val C = A.multiply(B)
        assertEquals(19.0, C.get(0, 0))
        assertEquals(22.0, C.get(0, 1))
        assertEquals(43.0, C.get(1, 0))
        assertEquals(50.0, C.get(1, 1))
    }

    @Test
    fun testStrassenMultiplication() {
        val A = Matrix(2)
        val B = Matrix(2)
        A.set(0, 0, 1.0); A.set(0, 1, 2.0)
        A.set(1, 0, 3.0); A.set(1, 1, 4.0)
        B.set(0, 0, 5.0); B.set(0, 1, 6.0)
        B.set(1, 0, 7.0); B.set(1, 1, 8.0)

        val normal = A.multiply(B)
        val strassen = A.strassenMultiply(B, cutoff = 1)

        for (i in 0 until 2)
            for (j in 0 until 2)
                assertEquals(normal.get(i, j), strassen.get(i, j),
                    "Strassen result should match standard multiplication")
    }

    @Test
    fun testLargerMatrixStrassen() {
        val A = Matrix(4)
        val B = Matrix(4)
        var counter = 1.0
        for (i in 0 until 4)
            for (j in 0 until 4) {
                A.set(i, j, counter)
                B.set(i, j, counter++)
            }

        val normal = A.multiply(B)
        val strassen = A.strassenMultiply(B, cutoff = 1)

        for (i in 0 until 4)
            for (j in 0 until 4)
                assertEquals(
                    normal.get(i, j),
                    strassen.get(i, j),
                    1e-9,
                    "Strassen should match normal multiplication for 4x4 matrices"
                )
    }
}
