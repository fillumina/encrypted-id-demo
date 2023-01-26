package com.fillumina.demo.encryptedid.shop.domain;

import com.github.f4b6a3.tsid.TsidFactory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A <a href='https://github.com/f4b6a3/tsid-creator'>TSID</a> is used as primary key. It shares all
 * the advantages of a <b>sequential UUID</b> (being itself a sequential universal identifier) and a
 * long id (being encoded in a long). It mimics the
 * <a href='https://en.wikipedia.org/wiki/Snowflake_ID'>SnowflakeId</a> created and used extensively
 * by Twitter and others.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Entity
public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;

    private final static TsidFactory TSID_FACTORY = TsidFactory.newInstance1024(0);

    @Id
    private Long id = TSID_FACTORY.create().toLong();

    /**
     * This foreign key (FK) will use the WebUser UUID primary key (PK) which is 128 bits long so
     * less performant (space wise but also somewhat in speed) than a standard 64 bit Long ID.
     */
    @ManyToOne
    private WebUser webUser;

    /**
     * Because {@link Item} is forced to use a quite inefficient {@link Item#hashCode() }
     * it is preferable to use a {@link List} to contain the collection.
     * <br>
     * List is also recommended by the
     * <a href='https://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html/ch20.html#performance-collections-mostefficentinverse'>
     * Hibernate documentation</a> in case of bidirectional relationships.
     */
    @OneToMany( cascade = { CascadeType.ALL } )
    private List<Item> items = new ArrayList<>();

    protected ShoppingCart() {
    }

    public ShoppingCart(final WebUser webUser) {
        this.webUser = Objects.requireNonNull(webUser, "webUser");
        // add to the collection of the new assigned user
        webUser.getShoppingCarts().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalCost() {
        return getItems().stream()
                .map(Item::getTotalPrice)
                .collect(Collectors.reducing(BigDecimal.ZERO,
                        (a, b) -> a.add(b)));
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof ShoppingCart)) {
            return false;
        }
        return Objects.equals(this.id, ((ShoppingCart) object).id);
    }

    @Override
    public String toString() {
        return "ShoppingCart{" + "id=" + id + '}';
    }

}
