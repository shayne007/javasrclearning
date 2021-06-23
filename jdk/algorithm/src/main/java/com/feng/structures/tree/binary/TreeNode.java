package com.feng.structures.tree.binary;

/**
 * @author fengsy
 * @date 6/22/21
 * @Description
 */
public class TreeNode {
    public int data;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {}

    public TreeNode(int data) {
        this.data = data;
    }

    public TreeNode(int data, TreeNode left, TreeNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }
}
