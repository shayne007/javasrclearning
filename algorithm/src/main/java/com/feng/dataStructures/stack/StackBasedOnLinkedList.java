package com.feng.dataStructures.stack;

/**
 * @author fengsy
 * @date 6/15/21
 * @Description
 */
public class StackBasedOnLinkedList {
    private Node top;
    private int size;

    Node createNode(String data, Node next) {
        return new Node(data, next);
    }

    public void clear() {
        this.top = null;
        this.size = 0;
    }

    public void push(String value) {
        Node newNode = new Node(value, this.top);
        this.top = newNode;
        this.size++;
    }

    public String pop() {
        Node node = this.top;
        if (node == null) {
            System.out.println("this stack is empty!");
            return null;
        }

        this.top = node.next;
        if (this.size > 0) {
            this.size--;
        }
        return node.getData();
    }

    public String getTopData() {
        if (this.top == null) {
            return null;
        }
        return this.top.data;
    }

    public int size() {
        return this.size;
    }

    public void print() {
        System.out.println("Print stack:");
        Node currentNode = this.top;
        while (currentNode != null) {
            String data = currentNode.getData();
            System.out.println(data);
            currentNode = currentNode.next;
        }
    }

    private static class Node {
        private String data;
        private Node next;

        public Node(String data) {
            this(data, null);
        }

        public Node(String data, Node next) {
            this.data = data;
            this.next = next;
        }

        public String getData() {
            return data;
        }
    }

    public static void main(String[] args) {
        StackBasedOnLinkedList stack = new StackBasedOnLinkedList();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        stack.pop();
        stack.push("D");
        stack.push("E");
        stack.pop();
        stack.push("F");
        stack.print();

        String data = stack.getTopData();
        System.out.println("Top data == " + data);
    }
}
