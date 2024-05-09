package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.misc.SimpleIterator;
import datastructures.worklists.CircularArrayFIFOQueue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * - You must implement a generic chaining hashtable. You may not
 *   restrict the size of the input domain (i.e., it must accept
 *   any key) or the number of inputs (i.e., it must grow as necessary).
 *
 * - ChainingHashTable should rehash as appropriate (use load factor as shown in lecture!).
 *
 * - ChainingHashTable must resize its capacity into prime numbers via given PRIME_SIZES list.
 *   Past this, it should continue to resize using some other mechanism (primes not necessary).
 *
 * - When implementing your iterator, you should NOT copy every item to another
 *   dictionary/list and return that dictionary/list's iterator.
 */
public class ChainingHashTable<K, V> extends DeletelessDictionary<K, V> {

    private Supplier<Dictionary<K, V>> newChain; // Used to make dictionarys
    private Dictionary<K, V>[] chains; // Array to hold the chains
    private int capacity; // Current capacity of the hash table
    private double loadFactor; // Load factor threshold for resizing
    private int resizeIndex;

    static final int[] PRIME_SIZES =
            {11, 23, 47, 97, 193, 389, 773, 1549, 3089, 6173, 12347, 24697, 49393, 98779, 197573, 395147};

    public ChainingHashTable(Supplier<Dictionary<K, V>> newChain) {
        this.newChain = newChain;
        resizeIndex = 0;
        capacity = PRIME_SIZES[resizeIndex]; // Start with the first prime size
        loadFactor = 0.75; // Default load factor threshold
        chains = (Dictionary<K, V>[]) new Dictionary[capacity];
        size = 0;
        for (int i = 0; i < capacity; i++) {
            chains[i] = newChain.get(); // Initialize each chain
        }
    }

    @Override
    public V insert(K key, V value) {
        int hash = key.hashCode() % capacity; // Calculate hash code and convert to positive
        // Dictionary<K, V> chain = chains[hash]; // Get the chain corresponding to the hash code

        // Insert the key-value pair into the chain
        V oldValue = chains[hash].insert(key, value);

        // Update size if a new key was inserted
        if (oldValue == null) {
            this.size++;
            System.out.println("Size: " + size());
        }

         // Check if resizing is needed
        if ((double) size / capacity > loadFactor) {
            // Increment resize then update prime size
            resizeIndex++;
            int newSize;
            if (resizeIndex < PRIME_SIZES.length) {
                newSize = PRIME_SIZES[resizeIndex];
            } else {
                newSize = capacity * 2 + 1; // beyond hardcoded primes, just double and add 1
            }

            // Create new hash table with new capacity, generate chain for each index
            Dictionary<K, V>[] newHashTable = (Dictionary<K, V>[]) new Dictionary[newSize];
            for (int i = 0; i < newSize; i++) {
                newHashTable[i] = newChain.get();
            }

            // Copy old data into new hash table
            Iterator<Item<K, V>> CHSIterator = this.iterator();
            while (CHSIterator.hasNext()) {
                Item<K, V> currItem = CHSIterator.next();
                int newHash = currItem.key.hashCode() % newSize;
                newHashTable[newHash].insert(currItem.key, currItem.value);
            }

            // Updates new hash table capacity and hash table itself
            capacity = newSize;
            chains = newHashTable;
        }

        // Returns old value
        return oldValue;
    }

    @Override
    public V find(K key) {
        int hash = key.hashCode() % capacity; // Calculate hash code and convert to positive

        // Find the key in the chain
        V value = chains[hash].find(key);

        return value;
    }

    // Method that will return iterator object
    @Override
    public Iterator<Item<K, V>> iterator() {
        return new ChainingHashTableIterator();
    }

    // The actual CHS iterator
    private class ChainingHashTableIterator extends SimpleIterator<Item<K, V>> {
        private int chainIndex;  // Index of the current chain
        private Iterator<Item<K, V>> chainIterator;  // Iterator for the current chain

        public ChainingHashTableIterator() {
            chainIndex = 0;
            chainIterator = chains[chainIndex].iterator();  // Start with the first chain
        }

        @Override
        public boolean hasNext() {
            // Check if the current chain has more elements
            if (chainIterator.hasNext()) {
                return true;
            }
            // Move to the next non-empty chain
            for (int i = chainIndex + 1; i < capacity; i++) {
                if (!chains[i].isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Item<K, V> next() {
            // Check if the current chain has more elements
            if (chainIterator.hasNext()) {
                return chainIterator.next();
            }
            // Move to the next non-empty chain
            for (int i = chainIndex + 1; i < capacity; i++) {
                if (!chains[i].isEmpty()) {
                    chainIndex = i;
                    chainIterator = chains[i].iterator();
                    return chainIterator.next();
                }
            }
            throw new NoSuchElementException();
        }
    }

    /**
     * Temporary fix so that you can debug on IntelliJ properly despite a broken iterator
     * Remove to see proper String representation (inherited from Dictionary)
     */
    @Override
    public String toString() {
        return "ChainingHashTable String representation goes here.";
    }
}
