interface AssociativeArray<K, V> {
    operator fun set(k: K, v: V)
    operator fun contains(k: K): Boolean
    operator fun get(k: K): V?
    fun remove(k: K): Boolean
    fun size(): Int
    fun keyValuePairs(): List<Pair<K, V>>
}
