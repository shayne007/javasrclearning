package com.feng.dataStructures.tree;

import com.feng.dataStructures.tree.binary.TreeNode;

/**
 * @author fengsy
 * @date 6/22/21
 * @Description
 */
public class LowestCommonAncestorBST {

    public TreeNode lowestCommonAncestorRecursive(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        if (root.data > p.data && root.data > q.data) {
            return lowestCommonAncestorRecursive(root.left, p, q);
        }
        if (root.data < p.data && root.data < q.data) {
            return lowestCommonAncestorRecursive(root.right, p, q);
        }
        return root;
    }

    public TreeNode lowestCommonAncestorIterative(TreeNode root, TreeNode p, TreeNode q) {
        while (root != null) {
            if (root.data > p.data && root.data > q.data) {
                root = root.left;
            }
            if (root.data < p.data && root.data < q.data) {
                root = root.right;
            }
            return root;
        }
        return root;
    }

    public static void main(String[] args) {
        int[] treeVals = {5, 3, 9, 14, 6, 10};
        LowestCommonAncestorBST lca = new LowestCommonAncestorBST();
        TreeNode ans = lca.lowestCommonAncestorRecursive(lca.insertLevelOrder(treeVals, new TreeNode(100), 0),
                new TreeNode(6), new TreeNode(11));
        System.out.println(ans.data);
    }

    public TreeNode insertLevelOrder(int[] arr, TreeNode root, int i) {
        // Base case for recursion
        if (i < arr.length) {
            TreeNode temp = new TreeNode(arr[i]);
            root = temp;

            // insert left child
            root.left = insertLevelOrder(arr, root.left, 2 * i + 1);

            // insert right child
            root.right = insertLevelOrder(arr, root.right, 2 * i + 2);
        }
        return root;
    }
}
