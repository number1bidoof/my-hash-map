import java.lang.foreign.ValueLayout;
import java.lang.module.FindException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MyHashMap<K, V> {

    // ── Inner entry class ─────────────────────────────────────────────────
    static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key   = key;
            this.value = value;
        }
    }

    // ── Fields ────────────────────────────────────────────────────────────
    private LinkedList<Entry<K, V>>[] table;
    private int size;
    private static final int DEFAULT_CAPACITY = 11;

    // ── Constructor ───────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new LinkedList[DEFAULT_CAPACITY];
        size  = 0;
    }

    // ── Hash helper ───────────────────────────────────────────────────────
    private int hash(K key) {
        // Keys must not be null
        return Math.abs(key.hashCode()) % table.length;
    }

    // ── put ───────────────────────────────────────────────────────────────
    public V put(K key, V value) {
        // TODO Step 1: compute the index using hash(key)

        // TODO Step 2: if table[index] is null, create a new LinkedList there

        // TODO Step 3: walk the list at table[index]
        //   -- compare keys using .equals(), not ==
        //   -- if an entry with the same key already exists, update its value
        //      and return the OLD value (do not increment size)

        // TODO Step 4: no existing entry found -- add a new Entry to the
        //   FRONT of the list (O(1) -- no traversal needed), increment size, return null
        int index = hash(key);
        if (table[index] == null){
            table[index] = new LinkedList<>();
        }
        for (Entry<K,V> entry : table[index]){
            if (entry.key.equals(table[index])){ // only loops through the list looking for an
                V og = entry.value;
                entry.value = value;
                return og;
            }
        }
        table[index].addFirst(new Entry<>(key, value));
        this.size++;
        return null;
    }

    // ── get ───────────────────────────────────────────────────────────────
    public V get(K key) {
        // TODO Step 1: compute the index using hash(key)

        // TODO Step 2: if table[index] is null, return null (key not present)

        // TODO Step 3: walk the list at table[index]
        //   -- if an entry with a matching key is found, return its value

        // TODO Step 4: key was not in the list -- return null
        int index = hash(key);
        if (table[index] == null)
            return null;
        for (Entry<K,V> entry : table[index]){
            if (entry.key.equals(key)){
                return entry.value;
            };
        }
        return null;
    }

    // ── containsKey ───────────────────────────────────────────────────────
    public boolean containsKey(K key) {
        // TODO: return true if the key exists in the map, false otherwise
        // Hint: get(key) returns null when the key is not present --
        //   but what if a key IS present and its value is null?
        //   Walk the list directly to be safe.
        int index = hash(key);
        if (table[index] == null)
            return false;
        for (Entry<K,V> entry : table[index]){
            if (entry.key.equals(key)){
                return true;
            }
        }
        return false;
    }

    // ── remove ────────────────────────────────────────────────────────────
    public V remove(K key) {
        // TODO Step 1: compute the index using hash(key)

        // TODO Step 2: if table[index] is null, return null (nothing to remove)

        // TODO Step 3: walk the list at table[index]
        //   -- compare keys using .equals(), not ==
        //   -- if an entry with a matching key is found:
        //      remove it from the list, decrement size, return its value
        //   -- You cannot remove from a list inside a for-each loop.
        //      Use an iterator: Iterator<Entry<K,V>> it = table[index].iterator()
        //      then it.remove() when you find the match.

        // TODO Step 4: key was not found -- return null
        int index = hash(key);
        if (table[index] == null){
            return null;
        }

        Iterator<Entry<K,V>> it = table[index].iterator();
        while (it.hasNext()){
            Entry<K, V> entry = it.next();
            if (entry.key.equals(key)){
                V oldvalue = entry.value;
                it.remove();
                this.size--;
                return oldvalue;
            }
        }
        return null; // replace this
    }

    // ── size ──────────────────────────────────────────────────────────────
    public int size() {
        return size;
    }

    // ── isEmpty ───────────────────────────────────────────────────────────
    public boolean isEmpty() {
        return size == 0;
    }

    // ── Test driver ───────────────────────────────────────────────────────
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        // put -- basic insertions
        map.put("cat", 1);
        map.put("dog", 2);
        map.put("rat", 3);
        map.put("bat", 4);
        map.put("ant", 5);
        System.out.println("Size after 5 insertions: " + map.size());   // 5

        // get -- existing keys
        System.out.println("get(cat): " + map.get("cat"));              // 1
        System.out.println("get(bat): " + map.get("bat"));              // 4

        // get -- missing key
        System.out.println("get(owl): " + map.get("owl"));              // null

        // put -- duplicate key (update)
        map.put("cat", 99);
        System.out.println("get(cat) after update: " + map.get("cat")); // 99
        System.out.println("Size after update: " + map.size());         // 5

        // containsKey
        System.out.println("containsKey(dog): " + map.containsKey("dog")); // true
        System.out.println("containsKey(elk): " + map.containsKey("elk")); // false

        // remove -- existing key
        map.remove("dog");
        System.out.println("get(dog) after remove: " + map.get("dog")); // null
        System.out.println("Size after remove: " + map.size());         // 4

        // remove -- missing key
        map.remove("owl");
        System.out.println("Size after removing missing key: " + map.size()); // 4

        // null value -- a key can legitimately map to null
        map.put("fox", null);
        System.out.println("get(fox): " + map.get("fox"));              // null
        System.out.println("containsKey(fox): " + map.containsKey("fox")); // true
        System.out.println("Size with null value: " + map.size());      // 5

        // forced collision -- "AaAaAa" and "BBBaaa" have the same hashCode in Java
        MyHashMap<String, Integer> collisionMap = new MyHashMap<>();
        collisionMap.put("AaAaAa", 1);
        collisionMap.put("BBBaaa", 2);
        System.out.println("Collision get(AaAaAa): " + collisionMap.get("AaAaAa")); // 1
        System.out.println("Collision get(BBBaaa): " + collisionMap.get("BBBaaa")); // 2
        collisionMap.remove("AaAaAa");
        System.out.println("After remove, get(BBBaaa): " + collisionMap.get("BBBaaa")); // 2
        System.out.println("After remove, size: " + collisionMap.size()); // 1
    }
}
