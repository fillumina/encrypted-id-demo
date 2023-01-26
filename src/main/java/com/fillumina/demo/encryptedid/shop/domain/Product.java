package com.fillumina.demo.encryptedid.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Products need to be searched by SKU so there must be a (secondary) index on that. These are the
 * disadvantages of using a string as a primary key (see
 * <a href='https://stackoverflow.com/questions/15477005/what-are-the-pros-and-cons-for-choosing-a-character-varying-data-type-for-primar'>
 * What are the pros and cons for choosing a character varying data type for primary key in SQL?</a>):
 * <ul>
 * <li>It takes much more space than usual big-integer (long)
 * <li>Comparing strings is much slower than to comparing numbers
 * <li>Using a big primary key causes a space amplification problem because it is repeated on all
 * other entities that refers to this one with foreign keys
 * <li>string takes more space on temporary structures such as views, indexes, caches etc
 * <li>Numeric indexes will be entered in order and minimal re-indexing will need to be done (i.e.
 * if you have a table with keys Apple, Carrot and want to insert Banana, the table will have to
 * move around the indexes so that Banana is inserted in the middle. You will rarely insert data in
 * the middle of an index if the index is numeric and sequential).
 * <li>Numeric indexes unlinked from data are not going to change.
 * <b>Do NOT use natural primary key</b>, for example passport number, social security number, or
 * employee contract number as these <b>can change in real world</b> situations.
 * </ul>
 *
 * For these reasons a possible work around could be to have a normal long primary key to use
 * internally (i.e. for FK) and a supplementary secondary index on SKU to be used when
 * accessed by the API (the only scenario that really needs it).
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Entity
@Table(name = "product", indexes = {
    @Index(name = "sku_idx", columnList = "sku")
})
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    // always prefer sequence because it allow for batch operations which are much faster
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Using SKU as secondary index accessible from the API
     */
    @Column(length = 50, unique = true, nullable = false)
    private String sku;

    private BigDecimal price;

    @OneToMany
    private List<Item> items = new ArrayList<>();

    protected Product() {
    }

    public Product(String sku, BigDecimal price) {
        this.sku = Objects.requireNonNull(sku, "sku");
        this.price = Objects.requireNonNull(price, "price");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        // being a natural key we only need SKU here
        return Objects.equals(this.sku, other.sku);
    }

    @Override
    public String toString() {
        return "Product{" + "sku=" + sku + ", price=" + price + '}';
    }
}
