package com.fillumina.demo.encryptedid.accounting.dto;

import java.math.BigDecimal;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ItemDTO {

    private PriceDTO product;
    private Integer quantity;

    public ItemDTO() {
    }

    public ItemDTO(String sku, String price, Integer quantity) {
        this.product = new PriceDTO(sku, new BigDecimal(price));
        this.quantity = quantity;
    }

    public PriceDTO getProduct() {
        return product;
    }

    public void setProduct(PriceDTO product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
