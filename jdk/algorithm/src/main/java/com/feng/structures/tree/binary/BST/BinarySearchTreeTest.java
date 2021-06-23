package com.feng.structures.tree.binary.BST;

import com.feng.structures.tree.binary.TreeNode;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fengsy
 * @date 6/23/21
 * @Description
 */
public class BinarySearchTreeTest {

    @Test
    public void testIsValidBST() {
        TreeNode tree = new TreeNode(2);
        tree.left = new TreeNode(1);
        tree.right = new TreeNode(3);
        BinarySearchTree bst = new BinarySearchTree(tree);
        Assert.assertTrue(bst.isValidBST());
    }
}
