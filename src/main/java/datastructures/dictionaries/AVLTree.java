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
     * Inner class to represent a node in the tree. Each node includes a data of
     * type E and an integer count. The class is protected so that subclasses of
     * BinarySearchTree can access it.
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
        root = insert(toAVLNode(root), key, value);
        return oldValue;
    }

    // Recursive helper insert function
    private AVLNode insert(AVLNode node, K key, V value) {
        // PART 1: INSERT AS NORMAL BST NODE
        // Perform standard BST insertion, when you hit null insert
        if (node == null) {
            oldValue = value;
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
            // Update value if key is same but value is different
            if (node.value != value) {
                node.value = value;
            }
            return node;
        }

        // PART 2: PERFORM CHECKS (AVL PROPERTY)

        // Update the height of the current node
        node.height = 1 + Math.max(getHeight(toAVLNode(node.children[0])), getHeight(toAVLNode(node.children[1])));

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

        // Recursively goes back up, and returns the current node (probably the children of some parent node)
        return node;
    }

    // Gets the height of the current node
    private int getHeight(AVLNode node) {
        if (node == null) {
            return 0;
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
        // Sets pointers
        AVLNode newRoot = toAVLNode(miniRoot.children[0]);
        AVLNode inBetweenNodes = toAVLNode(newRoot.children[0]);

        // Perform rotation
        newRoot.children[1] = miniRoot;
        miniRoot.children[0] = inBetweenNodes;

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
        newRoot.children[0] = miniRoot;
        miniRoot.children[1] = inBetweenNodes;

        // Update heights
        miniRoot.height = 1 + Math.max(getHeight(toAVLNode(miniRoot.children[0])), getHeight(toAVLNode(miniRoot.children[1])));
        newRoot.height = 1 + Math.max(getHeight(toAVLNode(newRoot.children[0])), getHeight(toAVLNode(newRoot.children[1])));

        // Return new root
        return newRoot;
    }
     

    /* THIS IS CODE TO VERIFY AVL TREE NEEDS TO BE UPDATED
    public static boolean verifyAVL(AVLNode root) {
         TODO: Edit this with your code
        if (root == null) {
        return true;
    } else {
        return (verifyBST(root, getMin(root), getMax(root)) &&
                verifyAVLHeight(root) && h(root));
    }
}
 TODO: Add private methods if you want (recommended)
// Verifies the height fields of nodes
public static boolean verifyNodeHeights(AVLNode root) {
    if (root == null) {
        return true;
    } else if (root.height == getHeights(root)) {
        return verifyNodeHeights(root.left) && verifyNodeHeights(root.right);
    } else {
        return false;
    }
}

// Gets the heights of nodes recursively
public static int getHeights(AVLNode root) {
    if (root == null) {
        return -1;
    } else {
        return (Math.max(getHeights(root.left), getHeights(root.right)) + 1);
    }
}

// Somehow make this o(n) or less time
public static boolean h (AVLNode root) {
    if (root == null) {
        return true;
    } else {
        if (verifyHeights(root) == -1) {
            return false;
        } else {
            return true;
        }
    }
}


public static int verifyHeights(AVLNode root) {
    if (root == null) {
        return -1;
    } else {
        int maxChildHeight = Math.max(verifyHeights(root.left), verifyHeights(root.right));
        if (root.height == maxChildHeight + 1) {
            return root.height;
        } else {
            return -1;
        }
    }
}


// Verifies the height property of an AVL tree
public static boolean verifyAVLHeight(AVLNode root) {
    if (root == null || (root.left == null && root.right == null)) {
        return true;
    } else if (root.left == null) {
        return verifyAVLHeight(root.right) && Math.abs(root.right.height) < 1;
    } else if (root.right == null) {
        return verifyAVLHeight(root.left) && Math.abs(root.left.height) < 1;
    } else if (Math.abs(root.left.height - root.right.height) < 2) {
        return verifyAVLHeight(root.left) && verifyAVLHeight(root.right);
    } else {
        return false;
    }
}

// Verify BST property
public static boolean verifyBST(AVLNode root, int min, int max) {
    if (root == null) {
        return true;
    } else if (min < root.key && root.key < max) {
        return (verifyBST(root.left, min, root.key) && verifyBST(root.right, root.key, max));
    } else {
        return false;
    }
}

// Gets min of tree for first iteration
public static int getMin(AVLNode root) {
    if (root.left != null) {
        return getMin(root.left);
    } else {
        return root.key - 1;
    }
}

// Gets max of tree for first iteration
public static int getMax(AVLNode root) {
    if (root.right != null) {
        return getMax(root.right);
    } else {
        return root.key + 1;
    }
}
     */
}
