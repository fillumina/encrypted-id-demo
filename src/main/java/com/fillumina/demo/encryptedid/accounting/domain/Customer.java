package com.fillumina.demo.encryptedid.accounting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * This entity represents a physical customer. It's the counterpart of the shop
 * {@link com.fillumina.demo.encryptedid.shop.domain.WebUser} and it
 * saves its (encrypted) UUID locally for cross references.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Entity
@Table(name = "customer", indexes = {
    @Index(name = "userId_idx", columnList = "userId")
})
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @Id
    // always prefer sequence because it allows fast batch operations
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * This UUID refers to the <b>Sequential UUID</b> that identifies a
     * {@link com.fillumina.demo.encryptedid.shop.domain.WebUser}.
     * Because it is sequential it can be easily guessed by an attacker and should be encrypted
     * before being sent outside the internal environment to avoid having other users being
     * exposed.
     */
    @JsonIgnore
    private UUID userId;

    @OneToMany
    public List<Invoice> invoices = new ArrayList<>();

    protected Customer() {
    }

    public Customer(UUID userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof Customer)) {
            return false;
        }
        return Objects.equals(this.id, ((Customer) object).id);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "Customer{" + "userId=" + userId + '}';
    }

}
