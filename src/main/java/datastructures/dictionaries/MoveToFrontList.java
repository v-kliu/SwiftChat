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
    private MTFLNode<K, V> head;
    private int size;

    private static class MTFLNode<K, V> {
        K key;
        V value;
        MTFLNode<K, V> next;

        public MTFLNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    public MoveToFrontList() {
        this.head = null;
        this.size = 0;
    }
    @Override
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Either key or value was null!");
        }

        MTFLNode<K, V> current = this.head;
        MTFLNode<K, V> prev = null;

        // Check if key already exists
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev != null) {
                    prev.next = current.next;
                    current.next = this.head;
                    this.head = current;
                }
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }
            prev = current;
            current = current.next;
        }

        // Key does not exist, insert at front
        MTFLNode<K, V> newNode = new MTFLNode<>(key, value);
        newNode.next = this.head;
        this.head = newNode;
        this.size++;
        return null;
    }

    public int size() {
        return this.size;
    }
    @Override
    public V find(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key was null!");
        }

        MTFLNode<K, V> current = head;
        MTFLNode<K, V> prev = null;

        // Search for key
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev != null) {
                    prev.next = current.next;
                    current.next = head;
                    head = current;
                }
                return current.value;
            }
            prev = current;
            current = current.next;
        }

        return null; // Key not found
    }

    @Override
    public Iterator<Item<K, V>> iterator() {
        return new MTFLIterator(this.head);
    }

    private class MTFLIterator extends SimpleIterator<Item<K, V>> {
        private MTFLNode<K, V> iteratorNode;

        public MTFLIterator(MTFLNode<K, V> headNode) {
            this.iteratorNode = headNode;
        }

        public boolean hasNext() {
            return this.iteratorNode != null;
        }

        public Item<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("List is empty!");
            }
            Item<K, V> item = new Item<>(this.iteratorNode.key, this.iteratorNode.value);
            this.iteratorNode = this.iteratorNode.next;
            return item;
        }
    }
}
