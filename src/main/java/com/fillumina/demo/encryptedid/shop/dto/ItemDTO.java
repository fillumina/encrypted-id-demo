package com.fillumina.demo.encryptedid.shop.dto;

import com.fillumina.demo.encryptedid.shop.domain.Item;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ItemDTO {

    private String sku;
    private int quantity;

    public ItemDTO() {
    }

    public ItemDTO(Item item) {
        this(item.getProduct().getSku(), item.getQuantity());
    }

    public ItemDTO(String sku, int quantity) {
        this.sku = sku;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "ItemDTO{" + "sku=" + sku + ", quantity=" + quantity + '}';
    }

}
