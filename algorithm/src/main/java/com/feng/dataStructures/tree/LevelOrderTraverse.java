package com.feng.dataStructures.tree;

import com.feng.dataStructures.tree.binary.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author fengsy
 * @date 6/23/21
 * @Description
 */
public class LevelOrderTraverse {
    public static List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<List<Integer>> result = new ArrayList<>();
        if (root == null)
            return result;
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> levelNodes = new ArrayList<>();
            while (size-- > 0) {
                TreeNode rnode = queue.poll();
                levelNodes.add(rnode.data);
                if (rnode.left != null)
                    queue.offer(rnode.left);
                if (rnode.right != null)
                    queue.offer(rnode.right);
            }
            result.add(levelNodes);
        }
        return result;
    }

    public static void main(String[] args) {
        TreeNode tree = new TreeNode(1);
        tree.left = new TreeNode(2);
        tree.right = new TreeNode(3);
        System.out.println(levelOrder(tree));
    }
}
