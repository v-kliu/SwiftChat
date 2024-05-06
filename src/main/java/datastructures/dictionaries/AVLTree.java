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

    // Helper function to cast any BSTNode to an AVLNode
    public AVLNode toAVLNode(BSTNode node) {
        return (AVLNode) node;
    }

    // Main insert function, will call helper function, returns old value if key already exists
    public V insert(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }

        root = insert(toAVLNode(root), key, value);
        return oldValue;
    }

    // Recursive helper insert function
    private AVLNode insert(AVLNode node, K key, V value) {
        // PART 1: INSERT AS NORMAL BST NODE
        // Perform standard BST insertion, when you hit null insert
        if (node == null) {
            size++;
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

        // Check balance
        int balance = checkBalance(node);

//        if (!isAVLTree()) {
//            System.out.println("failed insert of " + key + " with value " + value);
//            System.out.println("heights of children nodes are " + getHeight(toAVLNode(node.children[0])) + " and " + getHeight(toAVLNode(node.children[1])));
//            displayTree();
//        }

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

        // Update the height of the current node
        node.height = 1 + Math.max(getHeight(toAVLNode(node.children[0])), getHeight(toAVLNode(node.children[1])));

//        if (!isAVLTree()) {
//            System.out.println("2failed insert of " + key + " with value " + value);
//            System.out.println("2heights of children nodes are " + getHeight(toAVLNode(node.children[0])) + " and " + getHeight(toAVLNode(node.children[1])));
//            displayTree();
//            System.exit(0);
//        }

        // Recursively goes back up, and returns the current node (probably the children of some parent node)
        return node;
    }

    // Gets the height of the current node
    private int getHeight(AVLNode node) {
        if (node == null) {
            return -1; // i think bug has to do something w this
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
//        AVLNode newRoot = toAVLNode(miniRoot.children[0]);
//        AVLNode inBetweenNodes = toAVLNode(newRoot.children[0]);
//
//        // Perform rotation
//        newRoot.children[1] = miniRoot;
//        miniRoot.children[0] = inBetweenNodes;
        AVLNode newRoot = (AVLNode) miniRoot.children[0];
        miniRoot.children[0] = newRoot.children[1];
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
//        AVLNode newRoot = toAVLNode(miniRoot.children[1]);
//        AVLNode inBetweenNodes = toAVLNode(newRoot.children[0]);
//
//        // Perform rotation
//        newRoot.children[0] = miniRoot;
//        miniRoot.children[1] = inBetweenNodes;
        AVLNode newRoot = (AVLNode) miniRoot.children[1];
        miniRoot.children[1] = newRoot.children[0];
        newRoot.children[0] = miniRoot;

        // Update heights
        miniRoot.height = 1 + Math.max(getHeight(toAVLNode(miniRoot.children[0])), getHeight(toAVLNode(miniRoot.children[1])));
        newRoot.height = 1 + Math.max(getHeight(toAVLNode(newRoot.children[0])), getHeight(toAVLNode(newRoot.children[1])));

        // Return new root
        return newRoot;
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

    /*
    // THIS IS CODE TO VERIFY AVL TREE NEEDS TO BE UPDATED
    public boolean verifyAVL(AVLNode root) {
        if (root == null) {
            return true;
        } else {
            return (verifyBST(root, getMin(root), getMax(root)) &&
                    verifyAVLHeight(root) && h(root));
        }
    }
    // Verifies the height fields of nodes
    public boolean verifyNodeHeights(AVLNode root) {
        if (root == null) {
            return true;
        } else if (root.height == getHeights(root)) {
            return verifyNodeHeights(getLeftChild(root)) && verifyNodeHeights(getRightChild(root));
        } else {
            return false;
        }
    }

    // Gets the heights of nodes recursively
    public int getHeights(AVLNode root) {
        if (root == null) {
            return -1;
        } else {
            return (Math.max(getHeights(getLeftChild(root)), getHeights(getRightChild(root))) + 1);
        }
    }

    // Somehow make this o(n) or less time
    public boolean h (AVLNode root) {
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


    public int verifyHeights(AVLNode root) {
        if (root == null) {
            return -1;
        } else {
            int maxChildHeight = Math.max(verifyHeights(getLeftChild(root)), verifyHeights(getRightChild(root)));
            if (root.height == maxChildHeight + 1) {
                return root.height;
            } else {
                return -1;
            }
        }
    }


    // Verifies the height property of an AVL tree
    public boolean verifyAVLHeight(AVLNode root) {
        if (root == null || (getLeftChild(root) == null && getRightChild(root) == null)) {
            return true;
        } else if (getLeftChild(root) == null) {
            return verifyAVLHeight(getRightChild(root)) && Math.abs(getRightChild(root).height) < 1;
        } else if (getRightChild(root) == null) {
            return verifyAVLHeight(getLeftChild(root)) && Math.abs(getLeftChild(root).height) < 1;
        } else if (Math.abs(getLeftChild(root).height - getRightChild(root).height) < 2) {
            return verifyAVLHeight(getLeftChild(root)) && verifyAVLHeight(getRightChild(root));
        } else {
            return false;
        }
    }

    // Verify BST property
    public boolean verifyBST(AVLNode root, int min, int max) {
        if (root == null) {
            return true;
        } else if (min < root.key && root.key < max) {
            return (verifyBST(getLeftChild(root), min, root.key) && verifyBST(getRightChild(root), root.key, max));
        } else {
            return false;
        }
    }

    // Gets min of tree for first iteration
    public K getMin(AVLNode root) {
        if (getLeftChild(root) != null) {
            return getMin(getLeftChild(root));
        } else {
            return root.key;
        }
    }

    // Gets max of tree for first iteration
    public K getMax(AVLNode root) {
        if (getRightChild(root) != null) {
            return getMax(getRightChild(root));
        } else {
            return root.key;
        }
    }
    */
}
