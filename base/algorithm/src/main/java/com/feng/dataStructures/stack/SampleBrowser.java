package com.feng.dataStructures.stack;

/**
 * @author fengsy
 * @date 6/15/21
 * @Description
 */
public class SampleBrowser {
    private String currentPage;
    private StackBasedOnLinkedList backStack;
    private StackBasedOnLinkedList forwardStack;

    public SampleBrowser() {
        this.backStack = new StackBasedOnLinkedList();
        this.forwardStack = new StackBasedOnLinkedList();
    }

    public void open(String url) {
        if (currentPage != null) {
            this.backStack.push(this.currentPage);
            this.forwardStack.clear();
        }
        showUrl(url, "Open");
    }

    public boolean canGoBack() {
        return this.backStack.size() > 0;
    }

    public boolean canGoForward() {
        return this.forwardStack.size() > 0;
    }

    public String goBack() {
        if (canGoBack()) {
            this.forwardStack.push(this.currentPage);
            String backUrl = this.backStack.pop();
            showUrl(backUrl, "Back");
            return backUrl;
        }
        System.out.println("* can not go back, no page behind!");
        return null;
    }

    public String goForward() {
        if (canGoForward()) {
            this.backStack.push(this.currentPage);
            String forwardUrl = this.forwardStack.pop();
            showUrl(forwardUrl, "Forward");
            return forwardUrl;
        }
        System.out.println("** can not go forward, no page ahead!");
        return null;
    }

    public void showUrl(String url, String prefix) {
        this.currentPage = url;
        System.out.println(prefix + " page == " + url);
    }

    public void checkCurrentPage() {
        System.out.println("Current page is: " + this.currentPage);
    }

    public static void main(String[] args) {
        SampleBrowser browser = new SampleBrowser();
        browser.open("http://www.baidu.com");
        browser.open("http://news.baidu.com/");
        browser.open("http://news.baidu.com/ent");
        browser.goBack();
        browser.goBack();
        browser.goForward();
        browser.open("http://www.qq.com");
        browser.goForward();
        browser.goBack();
        browser.goForward();
        browser.goBack();
        browser.goBack();
        browser.goBack();
        browser.goBack();
        browser.checkCurrentPage();
    }
}
