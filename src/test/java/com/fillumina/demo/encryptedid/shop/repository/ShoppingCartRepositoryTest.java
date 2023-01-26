package com.fillumina.demo.encryptedid.shop.repository;

import com.fillumina.demo.encryptedid.shop.domain.Item;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
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
public class ShoppingCartRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    public void shouldHaveTheIdSetAfterCreation() {
        WebUser user = new WebUser("sputnik");
        em.persist(user);

        ShoppingCart cart = new ShoppingCart(user);
        final UUID userId = user.getId();

        assertThat(userId).isNotNull();
    }

    @Test
    public void shouldFindById() {
        WebUser user = new WebUser("sputnik");
        em.persist(user);

        ShoppingCart cart = new ShoppingCart(user);
        em.persist(cart);

        ShoppingCart find = shoppingCartRepository.findById(cart.getId()).orElseThrow();
        assertThat(find).isEqualTo(cart);
    }

    @Test
    public void shouldGetTheUser() {
        WebUser user = new WebUser("sputnik");
        em.persist(user);

        ShoppingCart cart = new ShoppingCart(user);
        em.persist(cart);

        ShoppingCart find = shoppingCartRepository.findById(cart.getId()).orElseThrow();
        assertThat(find.getWebUser()).isEqualTo(user);
    }

    @Test
    public void shouldFindAllTheItems() {
        WebUser user = em.persist(new WebUser("sputnik"));
        ShoppingCart cart = em.persist(new ShoppingCart(user));
        Product product1 = em.persist(new Product("T-SHIRT", new BigDecimal("12.5")));
        Product product2 = em.persist(new Product("SHORT", new BigDecimal("23.4")));

        Item item1 = em.persist(new Item(cart, product1, 12));
        Item item2 = em.persist(new Item(cart, product2, 12));

        ShoppingCart find = shoppingCartRepository.findById(cart.getId()).orElseThrow();

        assertThat(find.getItems())
                .hasSize(2)
                .contains(item1, item2);
    }

    @Test
    public void shouldCalculateTheTotal() {
        final BigDecimal shirtPrice = new BigDecimal("12.5");
        final BigDecimal shortPrice = new BigDecimal("23.4");
        final int shirtQuantity = 12;
        final int shortQuantity = 3;

        WebUser user = em.persist(new WebUser("sputnik"));
        ShoppingCart cart = em.persist(new ShoppingCart(user));
        Product shirtProduct = em.persist(new Product("T-SHIRT", shirtPrice));
        Product shortProduct = em.persist(new Product("SHORT", shortPrice));

        em.persist(new Item(cart, shirtProduct, shirtQuantity));
        em.persist(new Item(cart, shortProduct, shortQuantity));

        ShoppingCart find = shoppingCartRepository.findById(cart.getId()).orElseThrow();

        assertThat(find.getTotalCost())
                .isEqualTo(
                    shirtPrice.multiply(BigDecimal.valueOf(shirtQuantity))
                            .add(shortPrice.multiply(BigDecimal.valueOf(shortQuantity))));
    }
}
