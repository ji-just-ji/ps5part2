import com.sun.source.tree.Tree;

import java.util.*;

/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for inserting a node, and the code for
 * rebuilding a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        int weight = 1;
        public TreeNode left = null;
        public TreeNode right = null;

        TreeNode(int k) {
            key = k;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d)", key, weight);
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {

        // TODO: Implement this

        if(Child.LEFT == child){

            if(node.left == null){

                return 0;

            } else {

                return countAll(node.left);

            }

        } else {

            if(node.right == null){

                return 0;

            } else {

                return countAll(node.right);

            }

        }

    }
    public int countAll(TreeNode node){

        if(node.left == null && node.right == null){

            return 1;

        } else if(node.left == null){

            return 1 + countAll(node.right);

        } else if(node.right == null){

            return 1 + countAll(node.left);

        } else {

            return 1 + countAll(node.left) + countAll(node.right);

        }

    }



    /**
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */

    public TreeNode[] enumerateNodes(TreeNode node, Child child) {
        // TODO: Implement this
        int len = countNodes(node, child);
        TreeNode[] arr = new TreeNode[len];
        //O(n) time
        TreeNode next;
        if(child == Child.LEFT) {
            next = node.left;
        } else {
            next = node.right;
        }
        helper(next, arr, 0);
        return arr;
    }

    public int helper(TreeNode node, TreeNode[] arr, int index) {
        if(node == null) {return index;}
        int next = helper(node.left, arr, index);
        arr[next] = node;
        int output =  helper(node.right, arr, next+1);

        //System.out.println(Arrays.deepToString(arr));
        return output;

    }
    /**
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    public TreeNode buildTree(TreeNode[] nodeList) {

        // TODO: Implement this

        return buildTreeHelper(nodeList, 0, nodeList.length);

    }

    public TreeNode buildTreeHelper(TreeNode[] nodeList, int start, int end) {
        if(start >= end){
            return null;
        }
        int mid = start + (end - start)/2;
        TreeNode root = nodeList[mid];
        root.left = buildTreeHelper(nodeList, start, mid);
        root.right = buildTreeHelper(nodeList, mid+1, end);
        return root;
    }

    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        // TODO: Implement this

        if(node == null) {
            return true;
        }
        if(node.left == null && node.right == null) {
            return true;
        } else if(node.left == null) {
            return 3*node.right.weight <= 2*node.weight;
        } else if(node.right == null) {
            return 3*node.left.weight <= 2*node.weight;
        } else {
            return 3*node.left.weight <= 2*node.weight && 3*node.right.weight <= 2*node.weight;
        }

    }

    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
     * @param child specifies which child is the root of the subtree to rebuild
     */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        //System.out.println(Arrays.deepToString(nodeList));
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        //System.out.println(newChild.right);
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
        fixWeight(node, child);
    }

    private void fixWeight(TreeNode node, Child child) {

        if(child == Child.LEFT) {
            node = node.left;
        } else {
            node = node.right;
        }

        if(node.left == null && node.right == null) {
            node.weight = 1;
        } else if (node.left == null) {
            fixWeight(node, Child.RIGHT);
            node.weight = 1 + node.right.weight;
        } else if (node.right == null) {
            fixWeight(node, Child.LEFT);
            node.weight = 1 + node.left.weight;
        } else {
            fixWeight(node, Child.LEFT);
            fixWeight(node, Child.RIGHT);
            node.weight = 1 + node.right.weight + node.left.weight;
        }

    }

    /**
     * Inserts a key into the tree.
     *
     * @param key the key to insert
     */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;
        node.weight += 1;

        TreeNode prev = root;
        Child prevMove = null;

        TreeNode firstUnbalanced = null;
        Child path = null;

        while (true) {
            if(key <= node.key) {
                if(node.left == null) {
                    break;
                }
                node.left.weight += 1;
                if(checkBalance(node) && firstUnbalanced == null) {
                    firstUnbalanced = prev;
                    path = prevMove;
                }
                prev = node;
                node = node.left;
                prevMove = Child.LEFT;

            }
            if(key > node.key) {
                if(node.right == null) {
                    break;
                }

                node.right.weight += 1;
                if(checkBalance(node) && firstUnbalanced == null) {
                    firstUnbalanced = prev;
                    path = prevMove;
                }
                prev = node;
                node = node.right;
                prevMove = Child.RIGHT;


            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }

        //System.out.println(lastBalanced);
        //System.out.println(path);
        if(path == null) {
            TreeNode temp = new TreeNode(1);
            temp.left = root;
            rebuild(temp, Child.LEFT);
            root = temp.left;
        } else if(firstUnbalanced != null){
            rebuild(firstUnbalanced, path);
        }




    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        tree.root = new TreeNode(5);
        tree.insert(7);
        /*
        System.out.println(tree.root.weight);
        System.out.println(tree.root.right.weight);
        System.out.println((double) 7/11);
        System.out.println(tree.checkBalance(tree.root));


         */

        //System.out.println(tree.root);

        System.out.println(Arrays.deepToString(tree.enumerateNodes(tree.root, Child.LEFT)));
        System.out.println(Arrays.deepToString(tree.enumerateNodes(tree.root, Child.RIGHT)));

        System.out.println(tree.root);




    }
}
