/**
 * Implementation of an [AssociativeArray] using hash tables
 *
 * @param K the type of keys contained by the map
 * @param V the type of mapped values
 * @property total initial number of hash buckets (will grow as needed)
 */
class HashAssociativeArray<K, V>(
    private var total: Int = 11 // initial value (prime)
) : AssociativeArray<K, V> {

    // A single key–value pair stored within a bucket
    private data class Entry<K, V>(val key: K, var value: V)

    // Array of buckets; each bucket is a list of entries for the chaining
    private var buckets: Array<MutableList<Entry<K, V>>?> = arrayOfNulls(total)

    // Current number of key–value pairs in the hash table
    private var count = 0

    // Max load factor before rehashing
    private val loadFactorThreshold = 0.75

    /**
     * Inserts or updates the mapping from key [k] to value [v]
     * If the key already exists, the value is replaced
     *
     * @param k the key to insert or update
     * @param v the value to associate with [k]
     */
    override fun set(k: K, v: V) {
        // Check load factor and rehash if the table is too full
        if (count.toDouble() / total > loadFactorThreshold) rehash()

        val index = hashIndex(k)
        // Create a new bucket if it doesn't exist
        val bucket = buckets[index] ?: mutableListOf<Entry<K, V>>().also { buckets[index] = it }

        // If key already exists in the bucket, replace its value
        for (entry in bucket) {
            if (entry.key == k) {
                entry.value = v
                return
            }
        }

        // If not, insert new key–value pair
        bucket.add(Entry(k, v))
        count++
    }

    // Returns true if k is present in the associative array
    override operator fun contains(k: K): Boolean {
        val bucket = buckets[hashIndex(k)] ?: return false
        return bucket.any { it.key == k }
    }

    // Returns the value associated with k, null if the key is not present
    override operator fun get(k: K): V? {
        val bucket = buckets[hashIndex(k)] ?: return null
        return bucket.firstOrNull { it.key == k }?.value
    }

    /**
     * Removes the mapping for the specified key [k], if it exists
     *
     * @param k the key to remove
     * @return true if the key was found and removed, or else false
     */
    override fun remove(k: K): Boolean {
        val bucket = buckets[hashIndex(k)] ?: return false

        val iterator = bucket.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().key == k) {
                iterator.remove()
                count--
                return true
            }
        }
        return false
    }
    // Returns the number of key–value pairs stored in the hash table
    override fun size(): Int = count

    // Returns a list of all key–value pairs currently stored in the associative array
    override fun keyValuePairs(): List<Pair<K, V>> =
        buckets.filterNotNull()
            .flatten() // merge all lists
            .map { it.key to it.value } // map Entry<K,V> → Pair<K,V>

    /**
     * Computes the hash bucket index for the given key [k]
     * using division
     *
     * Takes the absolute value of the key's hash code for non-negative values
     */
    private fun hashIndex(k: K): Int {
        val hash = k?.hashCode() ?: 0
        val absHash = if (hash == Int.MIN_VALUE) 0 else kotlin.math.abs(hash)
        return absHash % total
    }

    // Rebuilds the hash table with a larger prime capacity when the load factor passes the threshold
    private fun rehash() {
        val oldBuckets = buckets

        // Double capacity and find next prime to minimize collision
        total = nextPrime(total * 2)
        buckets = arrayOfNulls(total)
        count = 0

        // Reinsert all entries into new table
        for (bucket in oldBuckets) {
            bucket?.forEach { set(it.key, it.value) }
        }
    }

    /**
     * Returns the next prime number greater than or equal to [n].
     * Used when resizing the hash table.
     */
    private fun nextPrime(n: Int): Int {
        var chosen_one = if (n % 2 == 0) n + 1 else n
        while (!isPrime(chosen_one)) chosen_one += 2
        return chosen_one
    }

    // Determines if x is a prime number.
    private fun isPrime(x: Int): Boolean {
        if (x <= 1) return false
        if (x <= 3) return true
        if (x % 2 == 0 || x % 3 == 0) return false

        var i = 5
        while (i.toLong() * i <= x) {
            if (x % i == 0 || x % (i + 2) == 0) return false
            i += 6
        }
        return true
    }
}
