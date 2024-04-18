package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;
import cse332.exceptions.NotYetImplementedException;
import cse332.interfaces.misc.DeletelessDictionary;
import cse332.interfaces.misc.SimpleIterator;
import datastructures.worklists.ArrayStack;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 1. The list is typically not sorted.
 * 2. Add new items to the front of the list.
 * 3. Whenever find or insert is called on an existing key, move it
 * to the front of the list. This means you remove the node from its
 * current position and make it the first node in the list.
 * 4. You need to implement an iterator. The iterator SHOULD NOT move
 * elements to the front.  The iterator should return elements in
 * the order they are stored in the list, starting with the first
 * element in the list. When implementing your iterator, you should
 * NOT copy every item to another dictionary/list and return that
 * dictionary/list's iterator.
 */
public class MoveToFrontList<K, V> extends DeletelessDictionary<K, V> {
    // Head and size fields, remember this is implemented with a linked list
    private MTFLNode<K, V> head;
    private int size;

    // Class for nodes in the linked list
    private static class MTFLNode<K, V> {
        // Fields of a node class
        K key;
        V value;
        MTFLNode<K, V> next;

        // Constructor of the node class
        public MTFLNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    // Constructor for MoveToFrontList data structure, is null and size is 0 at first
    public MoveToFrontList() {
        this.head = null;
        this.size = 0;
    }

    // Insert method
    @Override
    public V insert(K key, V value) {
        // Exception handling
        if (key == null || value == null) {
            throw new IllegalArgumentException("Either key or value was null!");
        }

        // Declares some pointers
        MTFLNode<K, V> current = this.head;
        MTFLNode<K, V> prev = null;

        // Check if key already exists
        while (current != null) {
            // If we get to a node with our key, we need to update and bring to front
            if (current.key.equals(key)) {
                // If our node isn't the first one in the linked list
                if (prev != null) {
                    // Rearrange pointers
                    prev.next = current.next;
                    current.next = this.head;
                    this.head = current;
                }
                // Get old value, update new value, and return old value
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            // Traverse linked list
            prev = current;
            current = current.next;
        }

        // Key does not exist (would have already exited), insert new node at front
        MTFLNode<K, V> newNode = new MTFLNode<>(key, value);
        newNode.next = this.head;
        this.head = newNode;
        this.size++;
        return null;
    }

    // Size function (was bugging out when inheriting from parent)
    public int size() {
        return this.size;
    }

    // Finds a key in the linked list, brings to front or returns null
    @Override
    public V find(K key) {
        // Exception handling
        if (key == null) {
            throw new IllegalArgumentException("Key was null!");
        }

        // Declares some pointers
        MTFLNode<K, V> current = head;
        MTFLNode<K, V> prev = null;

        // Search for key in the linked list
        while (current != null) {
            // If key matches
            if (current.key.equals(key)) {
                // If we aren't at the head
                if (prev != null) {
                    // Rearrange pointers
                    prev.next = current.next;
                    current.next = head;
                    head = current;
                }
                // Return the value at the node
                return current.value;
            }
            // Traverse linked list
            prev = current;
            current = current.next;
        }

        return null; // Key not found
    }

    // Returns new iterator (similar format to BinarySearchTree iterator)
    @Override
    public Iterator<Item<K, V>> iterator() {
        return new MTFLIterator(this.head);
    }

    // Class for my iterator
    private class MTFLIterator extends SimpleIterator<Item<K, V>> {
        // Field to keep track of where we are in linked list
        private MTFLNode<K, V> iteratorNode;

        // Constructor of iterator, sets first node to head
        public MTFLIterator(MTFLNode<K, V> headNode) {
            this.iteratorNode = headNode;
        }

        // Checks that current node exists (first node to next be returned)
        public boolean hasNext() {
            return this.iteratorNode != null;
        }

        // Next function will return current data and iterate
        public Item<K, V> next() {
            // Exception handling
            if (!hasNext()) {
                throw new NoSuchElementException("List is empty!");
            }
            // Gets the current item, iterates, and then returns data
            Item<K, V> item = new Item<>(this.iteratorNode.key, this.iteratorNode.value);
            this.iteratorNode = this.iteratorNode.next;
            return item;
        }
    }
}
