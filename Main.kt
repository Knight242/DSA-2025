// Worksheet 3 of SoftDes, 2023 -> Kotlin Practice

fun madLibPrompt(dictKey: Pair<Int, String>): String {
        print("Enter a ${dictKey.second}: ")
        val userInput = readLine() ?: ""
        return userInput.uppercase()
    }
fun enterMadLibs(madLibsDict: MutableMap<Pair<Int, String>, String>) {
    for (key in madLibsDict.keys) {
        madLibsDict[key] = madLibPrompt(key)
    }
}

fun generateStory(madLibsStory: String, madLibsDict: Map<Pair<Int, String>, String>): String {
    var story = madLibsStory
    for ((key, value) in madLibsDict) {
        val textBlank = "<${key.first}-${key.second}>"
        story = story.replace(textBlank, value)
    }
    return story
}

fun isPalindrome(str: String): Boolean {
    if (str.length <= 1) return true
    return str.first() == str.last() && isPalindrome(str.substring(1, str.length - 1))
}

fun power(x: Int, n: Int): Int {
    if (n == 0) return 1
    return x * power(x, n - 1)
}

fun factorial(n: Int): Int {
    if (n <= 1) return 1
    return n * factorial(n - 1)
}

fun <T> reverseList(list: List<T>): List<T> {
    if (list.isEmpty()) return list
    return reverseList(list.drop(1)) + list.first()
}

fun makeSalesTax(taxRate: Double): (Double) -> Double {
    return { price: Double -> price * (1 + taxRate) }
}

fun <T, R, V> compose(f: (R) -> V, g: (T) -> R): (T) -> V {
    return { x: T -> f(g(x)) }
}

// 4. Searching Algorithms

fun linearSearch(numList: List<Int>, target: Int): Int? {
    for ((index, value) in numList.withIndex()) {
        if (value == target) return index
    }
    return null
}

fun binarySearch(numList: List<Int>, target: Int): Int? {
    var low = 0
    var high = numList.size - 1
    while (low <= high) {
        val mid = (low + high) / 2
        when {
            numList[mid] == target -> return mid
            target < numList[mid] -> high = mid - 1
            else -> low = mid + 1
        }
    }
    return null
}
