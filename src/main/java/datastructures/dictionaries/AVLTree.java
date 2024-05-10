package datastructures.dictionaries;

import cse332.datastructures.containers.Item;
import cse332.datastructures.trees.BinarySearchTree;

import java.lang.reflect.Array;

/**
 * AVLTree must be a subclass of BinarySearchTree<E> and must use
 * inheritance and calls to superclass methods to avoid unnecessary
 * duplication or copying of functionality.
 * <p>
 * 1. Create a subclass of BSTNode, perhaps named AVLNode.
 * 2. Override the insert method such that it creates AVLNode instances
 * instead of BSTNode instances.
 * 3. Do NOT "replace" the children array in BSTNode with a new
 * children array or left and right fields in AVLNode.  This will
 * instead mask the super-class fields (i.e., the resulting node
 * would actually have multiple copies of the node fields, with
 * code accessing one pair or the other depending on the type of
 * the references used to access the instance).  Such masking will
 * lead to highly perplexing and erroneous behavior. Instead,
 * continue using the existing BSTNode children array.
 * 4. Ensure that the class does not have redundant methods
 * 5. Cast a BSTNode to an AVLNode whenever necessary in your AVLTree.
 * This will result a lot of casts, so we recommend you make private methods
 * that encapsulate those casts.
 * 6. Do NOT override the toString method. It is used for grading.
 * 7. The internal structure of your AVLTree (from this.root to the leaves) must be correct
 */

public class AVLTree<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {
    // TODO: Implement me!
    // Field to return old value if key already exists
    V oldValue;

    // Constructor for AVLTree
    public AVLTree() {
        oldValue = null;
    }

    /**
     * Inner class to represent a AVLnode in the AVLtree. Each node extends BSTNode
     * and simply just adds a height field since it an AVL.
     */
    public class AVLNode extends BSTNode {
        private int height;

        public AVLNode(K key, V value) {
            super(key, value);
            height = 0;
        }
    }

    // Helper function to cast any BSTNode to an AVLNode
    public AVLNode toAVLNode(BSTNode node) {
        return (AVLNode) node;
    }

    // Main insert function, will call helper function, returns old value if key already exists
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        oldValue = null;
        root = insert(toAVLNode(root), key, value);
        return oldValue;
    }

    // Recursive helper insert function
    private AVLNode insert(AVLNode node, K key, V value) {
        // PART 1: INSERT AS NORMAL BST NODE
        // Perform standard BST insertion, when you hit null insert, increments size and stores old value
        if (node == null) {
            size++;
            // oldValue = value;
            return new AVLNode(key, value);
        }

        // BST insertion
        if (key.compareTo(node.key) < 0) { // left
            node.children[0] = insert(toAVLNode(node.children[0]), key, value);
        } else if (key.compareTo(node.key) > 0) { // right
            node.children[1] = insert(toAVLNode(node.children[1]), key, value);
        } else { // equal
            // Duplicate keys are not allowed
            oldValue = node.value;
            // Update value either way
            node.value = value;
            return node;
        }

        // PART 2: PERFORM CHECKS (AVL PROPERTY)

        // Check balance
        int balance = checkBalance(node);

        // PART 3: ROTATE IF NECESSARY

        // Left Left case
        if (balance > 1 && key.compareTo(toAVLNode(node.children[0]).key) < 0) {
            return rightRotate(node);
        }

        // Right Right case
        if (balance < -1 && key.compareTo(toAVLNode(node.children[1]).key) > 0) {
            return leftRotate(node);
        }

        // Left Right case
        if (balance > 1 && key.compareTo(node.children[0].key) > 0) {
            node.children[0] = leftRotate(toAVLNode(node.children[0]));
            return rightRotate(node);
        }

        // Right Left case
        if (balance < -1 && key.compareTo(node.children[1].key) < 0) {
            node.children[1] = rightRotate(toAVLNode(node.children[1]));
            return leftRotate(node);
        }

        // Update the height of the current node if didn't rotate
        node.height = 1 + Math.max(getHeight(toAVLNode(node.children[0])), getHeight(toAVLNode(node.children[1])));

        // Recursively goes back up, and returns the current node (probably the children of some parent node)
        return node;
    }

    // Gets the height of the current node
    private int getHeight(AVLNode node) {
        if (node == null) {
            return -1; // height for a null child node
        } else {
            return node.height;
        }
    }

    // Returns the balance of the heights of the children node to assure AVL property
    private int checkBalance(AVLNode node) {
        if (node == null) {
            return 0;
        } else {
            return getHeight((AVLNode) node.children[0]) - getHeight((AVLNode) node.children[1]);
        }
    }

    // Rotates right (for left left case imagine)
    private AVLNode rightRotate(AVLNode miniRoot) {
//        // Sets pointers
        AVLNode newRoot = toAVLNode(miniRoot.children[0]);
        AVLNode inBetweenNodes = toAVLNode(newRoot.children[1]);

        // Perform rotation
        miniRoot.children[0] = inBetweenNodes;
        newRoot.children[1] = miniRoot;

        // Update heights
        miniRoot.height = 1 + Math.max(getHeight(toAVLNode(miniRoot.children[0])), getHeight(toAVLNode(miniRoot.children[1])));
        newRoot.height = 1 + Math.max(getHeight(toAVLNode(newRoot.children[0])), getHeight(toAVLNode(newRoot.children[1])));

        // Return new root
        return newRoot;
    }

    // Rotates left (for right right case imagine)
    private AVLNode leftRotate(AVLNode miniRoot) {
        // Sets pointers
        AVLNode newRoot = toAVLNode(miniRoot.children[1]);
        AVLNode inBetweenNodes = toAVLNode(newRoot.children[0]);

        // Perform rotation
        miniRoot.children[1] = inBetweenNodes;
        newRoot.children[0] = miniRoot;

        // Update heights
        miniRoot.height = 1 + Math.max(getHeight(toAVLNode(miniRoot.children[0])), getHeight(toAVLNode(miniRoot.children[1])));
        newRoot.height = 1 + Math.max(getHeight(toAVLNode(newRoot.children[0])), getHeight(toAVLNode(newRoot.children[1])));

        // Return new root
        return newRoot;
    }

    // EXTRA CODE
    // Helps print the tree for debugging purposes
    public void displayTree() {
        displayTree(toAVLNode(root), 0);
    }

    private void displayTree(AVLNode node, int level) {
        if (node != null) {
            displayTree(getRightChild(node), level + 1); // Traverse right subtree

            // Print the node with proper indentation
            for (int i = 0; i < level; i++) {
                System.out.print("    "); // 4 spaces for each level
            }
            System.out.println(node.key); // Print current node

            displayTree(getLeftChild(node), level + 1); // Traverse left subtree
        }
    }
     
    public AVLNode getLeftChild(AVLNode node) {
        if (node == null) {
            return null;
        } else {
            return toAVLNode(node.children[0]);
        }
    }

    public AVLNode getRightChild(AVLNode node) {
        if (node == null) {
            return null;
        } else {
            return toAVLNode(node.children[1]);
        }
    }


    // EXTRA CODE
    // VERIFIES AVL TREE
    public boolean isAVLTree() {
        return isAVLTree(toAVLNode(root), null, null);
    }

    private boolean isAVLTree(AVLNode node, K min, K max) {
        if (node == null) {
            return true;
        }

        // Check BST property
        if ((min != null && node.key.compareTo(min) <= 0) || (max != null && node.key.compareTo(max) >= 0)) {
            System.out.println("failed BST property at " + node.key);
            return false;
        }

        // Check AVL property
        int balance = checkBalance(node);
        if (Math.abs(balance) > 1) {
            System.out.println("failed AVL property at " + node.key);
            return false;
        }

        // Recursively check left and right subtrees
        return isAVLTree(toAVLNode(node.children[0]), min, node.key) && isAVLTree(toAVLNode(node.children[1]), node.key, max);
    }
}
