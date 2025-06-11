package com.feng.application.domain;

import java.io.Serializable;

import org.joda.money.Money;

/**
 * @author fengsy
 * @date 7/24/21
 * @Description
 */
public class Product implements Serializable {
    private long id;
    private String name;
    private String description;
    private Money price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name='" + name + '\'' + ", description='" + description + '\'' + ", price="
            + price + '}';
    }
}
