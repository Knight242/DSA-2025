/**
 * SmithWaterman.kt
 *
 * Implements the Smith–Waterman local alignment algorithm for DNA or protein sequences
 * The algorithm uses dynamic programming to find the 'best local alignment' between two sequences
 *
 * The score is calculated using match, mismatch, and gap penalties.
 * Backtracking gives back the highest scoring local alignment, returning both aligned
 * subsequences, their positions, and the alignment score
 *
 * Example:
 * val result = smithWaterman("GATTACA", "GGTTGCA")
 * println(result.score)
 * println(result.alignedA)
 * println(result.alignedB)
 *
 * Default scoring: match = +2, mismatch = -1, gap = -2
 */

/**
 * Represents the result of Smith–Waterman alignment.
 *
 * @property alignedA the locally aligned substring of sequence A (with gaps)
 * @property alignedB the locally aligned substring of sequence B (with gaps)
 * @property score the final alignment score of the best local match
 * @property startA starting index of the local alignment in sequence A
 * @property endA ending index of the local alignment in sequence A
 * @property startB starting index of the local alignment in sequence B
 * @property endB ending index of the local alignment in sequence B
 * @property identityPct percentage of matching characters in the aligned region
 */

data class SWResult(
    val alignedA: String,
    val alignedB: String,
    val score: Int,
    val startA: Int,
    val endA: Int,
    val startB: Int,
    val endB: Int,
    val identityPct: Double
)

/**
 * Performs the Smith–Waterman local sequence alignment
 *
 * @param a the first sequence
 * @param b the second sequence
 * @param match score for a matching pair of bases or amino acids (default +2)
 * @param mismatch penalty for a mismatch (default -1)
 * @param gap penalty for introducing a gap (default -2)
 * @return [SWResult] containing the alignment details (aligned strings, score, indices, identity)
 */

fun smithWaterman(a: String, b: String, match: Int = 2, mismatch: Int = -1, gap: Int = -2): SWResult {
    val n = a.length
    val m = b.length
    val H = Array(n + 1) { IntArray(m + 1) }
    val dir = Array(n + 1) { IntArray(m + 1) } // 0=stop,1=diag,2=up,3=left

    fun score(x: Char, y: Char) = if (x == y) match else mismatch

    var bestScore = 0
    var bestI = 0
    var bestJ = 0

    for (i in 1..n) {
        for (j in 1..m) {
            val diag = H[i - 1][j - 1] + score(a[i - 1], b[j - 1])
            val up = H[i - 1][j] + gap
            val left = H[i][j - 1] + gap
            val best = listOf(diag, up, left, 0).maxOrNull()!!
            H[i][j] = best
            dir[i][j] = when (best) {
                diag -> 1
                up -> 2
                left -> 3
                else -> 0
            }
            if (best > bestScore) {
                bestScore = best
                bestI = i
                bestJ = j
            }
        }
    }

    // Backtracking
    val revA = StringBuilder()
    val revB = StringBuilder()
    var i = bestI
    var j = bestJ
    var matches = 0
    var aligned = 0

    while (i > 0 && j > 0 && H[i][j] > 0) {
        when (dir[i][j]) {
            1 -> {
                val ca = a[i - 1]; val cb = b[j - 1]
                revA.append(ca); revB.append(cb)
                if (ca == cb) matches++
                aligned++
                i--; j--
            }
            2 -> { revA.append(a[i - 1]); revB.append('-'); i--; aligned++ }
            3 -> { revA.append('-'); revB.append(b[j - 1]); j--; aligned++ }
            else -> break
        }
    }

    return SWResult(
        alignedA = revA.reverse().toString(),
        alignedB = revB.reverse().toString(),
        score = bestScore,
        startA = i, endA = bestI,
        startB = j, endB = bestJ,
        identityPct = if (aligned == 0) 0.0 else 100.0 * matches / aligned
    )
}

fun main() {
    val result = smithWaterman(genomeSnippet, targetGenome)

    println("Smith–Waterman Results")
    println("Score: ${result.score}")
    println("testAgainst range: [${result.startA}, ${result.endA})")
    println("targetGenome range: [${result.startB}, ${result.endB})")
    println("Identity: ${"%.2f".format(result.identityPct)}%")
    println()
    println(result.alignedA)
    println(result.alignedB)
}
