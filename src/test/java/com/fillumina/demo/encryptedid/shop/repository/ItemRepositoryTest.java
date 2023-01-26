package com.fillumina.demo.encryptedid.shop.repository;

import com.fillumina.demo.encryptedid.shop.domain.Item;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@DataJpaTest
@Transactional
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ItemRepository ItemRepository;

    @Test
    public void shouldCheckForNullShoppingCart() {
        Product product = new Product("xyz", new BigDecimal("12.3"));
        assertThatThrownBy(() -> new Item(null, product, 0))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("shoppingCart");
    }

    @Test
    public void shouldCheckForNullProduct() {
        ShoppingCart cart = new ShoppingCart(new WebUser("pippo"));
        assertThatThrownBy(() -> new Item(cart, null, 0))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("product");
    }

    @Test
    public void shouldTheIdNotPresentAtCreationButCreatedByHibernateAfterPersisting() {
        WebUser user = em.persist(new WebUser("sputnik"));
        assertThat(user).isNotNull();

        ShoppingCart cart = em.persist(new ShoppingCart(user));
        assertThat(cart).isNotNull();

        Product product = em.persist(new Product("T-SHIRT", new BigDecimal("12.5")));
        assertThat(product).isNotNull();

        Item item = new Item(cart, product, 12);
        assertThat(item.getId()).isNull();
    }

    @Test
    public void shouldTheIdBePresentAfterPersisting() {
        WebUser user = em.persist(new WebUser("sputnik"));
        assertThat(user).isNotNull();

        ShoppingCart cart = em.persist(new ShoppingCart(user));
        assertThat(cart).isNotNull();

        Product product = em.persist(new Product("T-SHIRT", new BigDecimal("12.5")));
        assertThat(product).isNotNull();

        Item item = new Item(cart, product, 12);
        em.persist(item);
        assertThat(item.getId()).isNotNull();
    }

    @Test
    public void shouldGetTotalPrice() {
        final BigDecimal price = new BigDecimal("12.5");
        final int quantity = 12;

        WebUser user = em.persist(new WebUser("sputnik"));
        ShoppingCart cart = em.persist(new ShoppingCart(user));
        Product product = em.persist(new Product("T-SHIRT", price));
        Item item = em.persist(new Item(cart, product, quantity));

        assertThat(item.getTotalPrice())
                .isEqualTo(price.multiply(BigDecimal.valueOf(quantity)));
    }

    @Test
    public void shouldBeFindById() {
        WebUser user = em.persist(new WebUser("sputnik"));
        ShoppingCart cart = em.persist(new ShoppingCart(user));
        Product product = em.persist(new Product("T-SHIRT", new BigDecimal("12.5")));
        Item item = em.persist(new Item(cart, product, 12));

        Item find = ItemRepository.findById(item.getId()).orElseThrow();

        assertThat(find).isEqualTo(item);
    }

    @Test
    public void shouldGetValues() {
        WebUser user = em.persist(new WebUser("sputnik"));
        ShoppingCart cart = em.persist(new ShoppingCart(user));
        Product product = em.persist(new Product("T-SHIRT", new BigDecimal("12.5")));
        Item item = em.persist(new Item(cart, product, 12));

        Item find = ItemRepository.findById(item.getId()).orElseThrow();

        assertThat(find.getShoppingCart()).isEqualTo(cart);
        assertThat(find.getProduct()).isEqualTo(product);
        assertThat(find.getQuantity()).isEqualTo(12);
    }
}
