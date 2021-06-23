package com.feng.structures.tree;

import com.feng.structures.tree.binary.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 翻转二叉树
 * 
 * @author fengsy
 * @date 6/22/21
 * @Description
 */
public class InverseTree {

    public static TreeNode invertTree(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            TreeNode left = node.left;
            TreeNode right = node.right;

            TreeNode temp = left;
            node.left = right;
            node.right = temp;

            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }

        }
        return root;
    }

    public static TreeNode invertTree2(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode right = invertTree(root.right);
        TreeNode left = invertTree(root.left);
        root.left = right;
        root.right = left;
        return root;
    }

    public static void main(String[] args) {
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        System.out.println(tree);
    }
}
