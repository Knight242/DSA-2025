/**
 * Represents a single LZ78 token created during encoding.
 *
 * Each token corresponds to one dictionary addition:
 * - [index] refers to the position of the longest matching phrase
 *   already in the dictionary (0 means "empty string").
 * - [nextChar] is the next character in the input that extends that phrase.
 *
 * For example, given the string "ABA", one possible encoding might emit:
 *   (0, 'A'), (0, 'B'), (1, 'B'), (2, 'A')
 *
 * @property index Dictionary index of the longest prefix found.
 * @property nextChar The next character following the known prefix (null at end of input).
 */
data class LZ78Token(val index: Int, val nextChar: Char?)

/**
 * Implementation of the **Lempel–Ziv 1978 (LZ78)** compression algorithm.
 *
 * Contains only the encoding stage which converts a text string
 * into a list of (index, nextChar) pairs representing how the input can be
 * reconstructed using dictionary-based compression
 *
 * ### Thought Process
 * 1. Start with an empty dictionary
 * 2. Scan the input string from left to right
 * 3. Find the longest substring starting at the current position that exists in the dictionary
 * 4. Throw a token where:
 *  - index is the dictionary index of that substring
 *  - nextChar is the first new character after that substring
 * 5. Add the new substring (prefix + nextChar) to the dictionary
 * 6. Continue until the end of the input is reached
 *
 * The dictionary is stored in [AssociativeArray] implementation
 */
object LZ78Encoder {

    /**
     * Encodes a given [input] string into a sequence of LZ78 tokens.
     *
     * @param input The text to compress
     * @return A list of [LZ78Token] objects representing the compressed data
     */
    fun encode(input: String): List<LZ78Token> {
        val dict: AssociativeArray<String, Int> = HashAssociativeArray()
        var nextIndex = 1
        // Output token list
        val tokens = mutableListOf<LZ78Token>()

        var i = 0 // current position in the input

        while (i < input.length) {
            var length = 0         // length of the current matching substring
            var lastIndex = 0      // dictionary index of the longest known prefix (0 = empty)

            // Find longest substring starting at position i that already
            // exists in the dictionary
            while (i + length <= input.length) {
                val substring = input.substring(i, i + length)

                // Get index if it exists or mark as -1 if not found
                val idx = dict[substring] ?: if (substring.isEmpty()) 0 else -1

                if (idx == -1) break // no longer match found, stop
                lastIndex = idx      // update prefix index
                length++
            }

            // Identify the next character following the known prefix
            val nextPos = i + length - 1
            // If we've reached the end of the string, nextChar = null.
            val nextChar = if (nextPos < input.length) input[nextPos] else null

            // Throw token
            tokens.add(LZ78Token(lastIndex, nextChar))

            // Add the new phrase (prefix + nextChar) to the dictionary
            // but only if there is a nextChar (not at end of input)
            if (nextChar != null) {
                val newPhrase = input.substring(i, nextPos + 1)
                if (newPhrase !in dict) {
                    dict[newPhrase] = nextIndex++
                }
            }

            // Move forward by the number of characters encoded
            i += length
        }

        return tokens
    }
}

fun main() {
    val testInputs = listOf(
        "AAAAAA",  // repetitive
        "ABABABA", // alternating
        "TOBEORNOTTOBEORTOBEORNOT", // classic LZ78 example
        "the quick brown fox jumps over the lazy dog the quick brown fox", //  text
        "XYZ123!@#" // random pattern
    )
    for (input in testInputs) {
        println("------------------------------------------------------------")
        println("Input: \"$input\"")
        println("Length: ${input.length}")

        val tokens = LZ78Encoder.encode(input)
        println("Tokens produced: ${tokens.size}")

        println("Encoded Tokens:")
        tokens.forEachIndexed { idx, token ->
            println("  ${idx + 1}. (index=${token.index}, nextChar=${token.nextChar ?: "∅"})")
        }
        println()
    }
}

