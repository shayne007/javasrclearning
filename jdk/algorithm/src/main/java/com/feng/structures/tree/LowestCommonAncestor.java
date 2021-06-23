package com.feng.structures.tree;

import com.feng.structures.tree.binary.TreeNode;

/**
 * Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.
 * 
 * https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
 * 
 * @author fengsy
 * @date 6/22/21
 * @Description
 */
public class LowestCommonAncestor {
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root.data == p.data || root.data == q.data)
            return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        return left == null ? right : (right == null ? left : root);
    }

    public static void main(String[] args) {
        int[] treeVals = {1, 2, 3, 4, 5, 6, 7, 8};
        LowestCommonAncestor lca = new LowestCommonAncestor();
        TreeNode ans = lca.lowestCommonAncestor(lca.insertLevelOrder(treeVals, new TreeNode(1), 0), new TreeNode(7),
            new TreeNode(8));
        System.out.println(ans.data);
        lca.inOrder(ans);
    }

    public void inOrder(TreeNode root) {
        if (root != null) {
            inOrder(root.left);
            System.out.print(root.data + " ");
            inOrder(root.right);
        }
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
