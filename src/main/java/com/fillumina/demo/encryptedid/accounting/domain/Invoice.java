package com.fillumina.demo.encryptedid.accounting.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fillumina.keyencryptor.jackson.EncryptableLongAsUuid;
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

    public static final long ENCRYPTED_FIELD_ID = 567L;

    /**
     * Invoices will not be accessed individually from outside so they don't need a public
     * identifier and a much more efficient default Long can be used instead.
     */
    @EncryptableLongAsUuid(ENCRYPTED_FIELD_ID)
    @Id
    // always prefer sequence because it allows fast batch operations
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String shoppingCartId;

    private BigDecimal total;

    @JsonIgnoreProperties(
        value = { "invoices" },
        allowSetters = true
    )
    @ManyToOne
    private Customer customer;

    protected Invoice() {
    }

    public Invoice(String shoppingCartId, Customer customer, BigDecimal total) {
        this.shoppingCartId = shoppingCartId;
        this.total = total;
        this.customer = customer;
        this.customer.getInvoices().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getShoppingCartId() {
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
