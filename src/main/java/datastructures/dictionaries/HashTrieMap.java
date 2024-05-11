package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.interfaces.misc.Dictionary;
import cse332.interfaces.trie.TrieMap;
import cse332.types.BString;

import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * See cse332/interfaces/trie/TrieMap.java
 * and cse332/interfaces/misc/Dictionary.java
 * for method specifications.
 */
public class HashTrieMap<A extends Comparable<A>, K extends BString<A>, V> extends TrieMap<A, K, V> {
    // Class for HashTrieNode
    public class HashTrieNode extends TrieNode<Dictionary<A, HashTrieNode>, HashTrieNode> {
        public HashTrieNode() {
            this(null);
        }

        public HashTrieNode(V value) {
            // Could modify which data structure to use for ChainingHashTable to test
            this.pointers = new ChainingHashTable<>(AVLTree::new);
            this.value = value;
        }

        @Override
        public Iterator<Entry<A, HashTrieMap<A, K, V>.HashTrieNode>> iterator() {
            // Iterator (needs to change entry to item type)
            return new Iterator<Entry<A, HashTrieNode>>() {
                Iterator<Item<A, HashTrieNode>> pointersItr = pointers.iterator();
                @Override
                public boolean hasNext() {
                    return pointersItr.hasNext();
                }

                @Override
                public Entry<A, HashTrieNode> next() {
                    // Error checking
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    // Converts Item to Entry
                    Item<A, HashTrieNode> next = pointersItr.next();
                    Entry<A, HashTrieNode> entry = new SimpleEntry<>(next.key, next.value);
                    return entry;
                }
            };
        }
    }

    public HashTrieMap(Class<K> KClass) {
        super(KClass);
        this.root = new HashTrieNode();
    }

    @Override
    public V insert(K key, V value) {
        // Throw exception if either key or value is null as per spec
        if (key == null || value == null) {
            throw new IllegalArgumentException("Either key or value was null when inserting!");
        }

        // Declares currentNode used to traverse Trie
        HashTrieNode currentNode = (HashTrieNode) this.root;
        // Declares previousValue as a placeholder for later
        V previousValue = null;

        // For each loop to iterate for each character of the key
        for (A character : key) {
            // Used for the childNode of the currentNode, gets using the current character
            // of key
            HashTrieNode childNode = (HashTrieNode) currentNode.pointers.find(character);

            // If that childNode (current character of key is not in hashmap of currentNode)
            // then, create a newNode, set currentNode pointer appropriately
            if (childNode == null) {
                childNode = new HashTrieNode();
                currentNode.pointers.insert(character, childNode);
            }

            // Set the currentNode to childNode to iterate for next character
            currentNode = childNode;
        }
        // Sets previousValue to whatever the last currentNode (could be null if we
        // just inserted a new node for the character)
        previousValue = currentNode.value;

        if (previousValue == null) {
            size++;
        }

        // Update the currentNodes value with the value needed to be inserted
        currentNode.value = value;
        // Return the previousValue as per spec
        return previousValue;
    }

    @Override
    public V find(K key) {
        // Throws an error if passed key is null
        if (key == null) {
            throw new IllegalArgumentException("Key passed was null!");
        }

        // Sets currentNode to root to traverse Trie
        HashTrieNode currentNode = (HashTrieNode) this.root;

        // For each character in key
        for (A character : key) {
            // Find the childNode using current character
            HashTrieNode childNode = (HashTrieNode) currentNode.pointers.find(character);
            // If the childNode is null (dosen't exist), then return null
            if (childNode == null) {
                return null;
            }
            // Else, set currentNode to childNode and continue traversing
            currentNode = childNode;
        }
        // Return the currentNode's value (the node for the key)
        return currentNode.value;
    }

    @Override
    public boolean findPrefix(K key) {
        // Throws an error if passed key is null
        if (key == null) {
            throw new IllegalArgumentException("Key passed was null!");
        }

        // Sets currentNode to root to traverse Trie
        HashTrieNode currentNode = (HashTrieNode) this.root;

        // For each character in key
        for (A character : key) {
            // Find the childNode using current character
            HashTrieNode childNode = (HashTrieNode) currentNode.pointers.find(character);
            // If the childNode is null (dosen't exist), then return null
            if (childNode == null) {
                return false;
            }
            // Else, set currentNode to childNode and continue traversing
            currentNode = childNode;
        }

        // If still haven't exited, then check for edge case that we are still at root,
        // map is empty and root is null, if passes then the key must exist in Trie, return true
        if (currentNode == (HashTrieNode) this.root && currentNode.value == null && size == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void delete(K key) {
        throw new UnsupportedOperationException("Not supported yet.");

        /*
        // Throws an error if passed key is null
        if (key == null) {
            throw new IllegalArgumentException("Key passed was null!");
        }

        // 1. has value
        // 2. has children
        // 3. is root
        // find -> get rid of value -> perform checks -> (return or go back to last node to perform checks)

        // Checks that key exists, only deletes if key exists
        if (findPrefix(key)) {
            // Could run if we are at root and value is not null of if we are at root value is null but map is not empty

            // Sets currentNode to root to traverse Trie
            HashTrieNode currentNode = (HashTrieNode) this.root;

            // Creates stack to keep track of Trie
            ArrayStack<HashTrieNode> trieNodes = new ArrayStack<>();
            ArrayStack<A> keyStack = new ArrayStack<>();
            trieNodes.add(currentNode);

            // For each character in key
            for (A character : key) {
                // Find the childNode using current character and update currentNode
                currentNode = (HashTrieNode) currentNode.pointers.find(character);
                trieNodes.add(currentNode);
                keyStack.add(character);
            }

            // If we get to our key node, but it is null, no need to decrease size just return
            if (currentNode.value == null) {
                return;
            }
            // Else, continue with deletion

            // 1.
            currentNode.value = null;

            int counter = 0;
            size--;
            // String keyString = key.toString();

            // 2. Perform checks
            while (currentNode.value == null && currentNode.pointers.isEmpty() && currentNode != this.root) {
                // Deletes last node and decrements size
                trieNodes.next();

                // Gets parent node, updates currentNode, remove pointer from parent node
                HashTrieNode parentNode = trieNodes.peek();
                parentNode.pointers.delete(keyStack.next());
                currentNode = parentNode;
            }
        }
        */
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");

        /*
        // Resets tree and sets size to 0
        this.root = new HashTrieNode();
        size = 0;
        */
    }
}
