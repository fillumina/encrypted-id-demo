package com.fillumina.demo.encryptedid.shop.dto;

import java.math.BigDecimal;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ProductDTO {
    private String sku;
    private BigDecimal price;

    public ProductDTO() {
    }

    public ProductDTO(String sku, BigDecimal price) {
        this.sku = sku;
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
