package com.fillumina.demo.encryptedid.accounting.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class InvoiceDTO {

    private UUID customerId;
    private Long shoppingCartId;
    BigDecimal total;

    public InvoiceDTO() {
    }

    public InvoiceDTO customerId(final UUID value) {
        this.customerId = value;
        return this;
    }

    public InvoiceDTO shoppingCartId(final Long value) {
        this.shoppingCartId = value;
        return this;
    }

    public InvoiceDTO total(final BigDecimal value) {
        this.total = value;
        return this;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "customerId=" + customerId +
                ", shoppingCartId=" + shoppingCartId +
                ", total=" + total + '}';
    }

}
