package com.feng.dataStructures.tree;

import com.feng.dataStructures.tree.binary.TreeNode;

/**
 * 路径总和
 *
 * @author fengsy
 * @date 6/22/21
 * @Description
 */
public class PathSum {
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null)
            return false;
        if (root.left == null && root.right == null && (root.data - targetSum) == 0)
            return true;
        // searching in the left half of the tree
        boolean leftResult = hasPathSum(root.left, targetSum - root.data);
        if (leftResult == true)
            return true;
        boolean rightResult = hasPathSum(root.right, targetSum - root.data);
        if (rightResult == true)
            return true;
        return false;
    }
}
