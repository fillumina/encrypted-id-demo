package com.fillumina.demo.encryptedid.accounting.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Entity
public class Invoice implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Invoices will not be accessed individually from outside so they don't need a public
     * identifier and a much more efficient default Long can be used instead.
     */
    @Id
    // always prefer sequence because it allows batch operations that are much faster
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long shoppingCartId;

    private BigDecimal total;

    @ManyToOne
    private Customer customer;

    protected Invoice() {
    }

    public Invoice(Long shoppingCartId, Customer customer, BigDecimal total) {
        this.shoppingCartId = shoppingCartId;
        this.total = total;
        this.customer = customer;
        this.customer.getInvoices().add(this);
    }

    public Long getId() {
        return id;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof Invoice)) {
            return false;
        }
        return Objects.equals(this.id, ((Invoice) object).id);
    }

    @Override
    public int hashCode() {
        return shoppingCartId.hashCode();
    }

    @Override
    public String toString() {
        return "Invoice{" + "shoppingCartId=" + shoppingCartId + ", total=" + total + '}';
    }

}
