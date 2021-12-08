package com.feng.dataStructures.linkedlist;

/**
 * @author fengsy
 * @date 6/15/21
 * @Description
 */
public class LinkedListAlgo {
    // 单链表反转
    public static Node reverse(Node list) {
        Node curr = list, pre = null;
        while (curr != null) {
            Node next = curr.next;
            curr.next = pre;
            pre = curr;
            curr = next;
        }
        return pre;
    }

    // 检测环
    public static boolean checkCircle(Node list) {

        if (list == null) {
            return false;
        }
        Node fast = list.next;
        Node slow = list;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;

    }

    // 有序链表合并 Leetcode 21
    /**
     * Definition for singly-linked list.
     * 
     */
    public class ListNode {
        int val;
        ListNode next;

        ListNode(int value) {
            val = value;
        }
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode soldier = new ListNode(0); // 利用哨兵结点简化实现难度 技巧三
        ListNode p = soldier;

        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                p.next = l1;
                l1 = l1.next;
            } else {
                p.next = l2;
                l2 = l2.next;
            }
            p = p.next;
        }

        if (l1 != null) {
            p.next = l1;
        }
        if (l2 != null) {
            p.next = l2;
        }
        return soldier.next;
    }

    // 删除倒数第K个节点
    public static Node deleteLastKth(Node list, int k) {
        Node fast = list;
        int i = 1;
        while (fast != null && i < k) {
            fast = fast.next;
            ++i;
        }
        if (fast == null)
            return list;
        Node slow = list;
        Node pre = null;
        while (fast.next != null) {
            fast = fast.next;
            pre = slow;
            slow = slow.next;
        }

        if (pre == null) {
            list = list.next;
        } else {
            pre.next = pre.next.next;
        }
        return list;
    }

    // 求中间节点
    public static Node findMiddleNode(Node list) {
        if (list == null)
            return null;
        Node fast = list;
        Node slow = list;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    public static void printAll(Node list) {
        Node p = list;
        while (p != null) {
            System.out.print(p.data + " ");
            p = p.next;
        }
        System.out.println();
    }

    public static Node createNode(int value) {
        return new Node(value, null);
    }

    public static class Node {
        private int data;
        private Node next;

        public Node(int data, Node next) {
            this.data = data;
            this.next = next;
        }

        public int getData() {
            return data;
        }
    }

    public static void main(String[] args) {
        Node list = new Node(1, null);
        list.next = new Node(3, new Node(5, null));
        printAll(list);

        Node rev = reverse(list);
        printAll(list);
        printAll(rev);
    }
}
