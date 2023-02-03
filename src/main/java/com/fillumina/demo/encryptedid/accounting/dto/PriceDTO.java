package com.fillumina.demo.encryptedid.accounting.dto;

import java.math.BigDecimal;

/**
 * Mimic {@link com.fillumina.demo.encryptedid.shop.domain.Product}
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class PriceDTO {
    private String sku;
    private BigDecimal price;

    public PriceDTO() {
    }

    public PriceDTO(String sku, BigDecimal price) {
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
