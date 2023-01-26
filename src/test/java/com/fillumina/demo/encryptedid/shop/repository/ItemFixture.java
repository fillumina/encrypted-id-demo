package com.fillumina.demo.encryptedid.shop.repository;

import com.fillumina.demo.encryptedid.shop.domain.Item;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import java.math.BigDecimal;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ItemFixture {

    private String login;
    private String sku;
    private String price;
    private int quantity;

    private ShoppingCart shoppingCart;
    private Product product;

    public static ItemFixture builder() {
        return new ItemFixture();
    }

    public ItemFixture login(final String value) {
        this.login = value;
        return this;
    }

    public ItemFixture sku(final String value) {
        this.sku = value;
        return this;
    }

    public ItemFixture price(final String value) {
        this.price = value;
        return this;
    }

    public ItemFixture quantity(final int value) {
        this.quantity = value;
        return this;
    }

    public ItemFixture shoppingCart(final ShoppingCart value) {
        this.shoppingCart = value;
        return this;
    }

    public ItemFixture product(final Product value) {
        this.product = value;
        return this;
    }

    public Item build(TestEntityManager em) {
        WebUser user = em.persist(new WebUser(login));
        ShoppingCart cart = this.shoppingCart != null
                ? this.shoppingCart
                : em.persist(new ShoppingCart(user));
        Product product = this.product != null
                ? this.product
                : em.persist(new Product(sku, new BigDecimal(price)));
        Item item = em.persist(new Item(cart, product, quantity));
        return item;
    }
}
