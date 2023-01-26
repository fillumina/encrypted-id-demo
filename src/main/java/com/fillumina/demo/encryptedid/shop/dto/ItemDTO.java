package com.fillumina.demo.encryptedid.shop.dto;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ItemDTO {

    private String sku;
    private int quantity;

    public ItemDTO() {
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
