package com.fillumina.demo.encryptedid.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * An Item will never be accessed individually but always reported along with an {@link Invoice}
 * so there is no need for an universal id and a much more efficient default {@link Long} can be
 * used instead.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Entity
public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Item doesn't need to be addressed individually so it's using a default long ID.
     */
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Integer quantity;

    @ManyToOne
    private Product product;

    @JsonIgnore
    @ManyToOne
    private ShoppingCart shoppingCart;

    protected Item() {
    }

    public Item(ShoppingCart shoppingCart, Product product, Integer quantity) {
        Objects.requireNonNull(shoppingCart, "shoppingCart");
        Objects.requireNonNull(product, "product");

        this.shoppingCart = shoppingCart;
        this.shoppingCart.getItems().add(this);

        this.product = product;
        this.product.getItems().add(this);

        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    @JsonIgnore
    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof Item)) {
            return false;
        }
        return Objects.equals(this.id, ((Item) object).id);
    }

    @Override
    public String toString() {
        return "Item { id=" + id + " }";
    }

}
