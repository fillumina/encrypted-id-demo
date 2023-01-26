package com.fillumina.demo.encryptedid.shop.dto;

import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ShoppingCartDTO {

    private String userId;
    private List<ItemDTO> items;

    public ShoppingCartDTO userId(final String value) {
        this.userId = value;
        return this;
    }

    public ShoppingCartDTO items(final List<ItemDTO> value) {
        this.items = value;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ShoppingCartDTO{" + "userId=" + userId + ", items=" + items + '}';
    }

}
