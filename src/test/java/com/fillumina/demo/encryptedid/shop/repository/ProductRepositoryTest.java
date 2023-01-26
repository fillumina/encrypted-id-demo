package com.fillumina.demo.encryptedid.shop.repository;

import com.fillumina.demo.encryptedid.shop.domain.Item;
import com.fillumina.demo.encryptedid.shop.domain.Product;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
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
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldCheckConstructorParameterSkuBeingNotNull() {
        Assertions.assertThatThrownBy(() -> new Product(null, BigDecimal.TEN))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("sku");
    }

    @Test
    public void shouldCheckConstructorParameterPriceBeingNotNull() {
        Assertions.assertThatThrownBy(() -> new Product("xyz", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("price");
    }

    @Test
    public void shouldNotAddTheIdBeforePersisting() {
        Product product = new Product("T-SHIRT", new BigDecimal("3.45"));
        assertThat(product.getId()).isNull();
    }

    @Test
    public void shouldIdBeAvailableAfterPersisting() {
        Product product = new Product("T-SHIRT", new BigDecimal("3.45"));
        em.persist(product);
        assertThat(product.getId()).isNotNull();
    }

    @Test
    public void shouldFindById() {
        Product product1 = em.persist(new Product("T-SHIRT", new BigDecimal("3.45")));
        Product product2 = em.persist(new Product("CAP", new BigDecimal("0.99")));

        Product find = productRepository.findById(product2.getId()).orElseThrow();

        assertThat(find).isEqualTo(product2);
    }

    @Test
    public void shouldFindBySku() {
        Product product1 = em.persist(new Product("T-SHIRT", new BigDecimal("3.45")));
        Product product2 = em.persist(new Product("CAP", new BigDecimal("0.99")));

        Product find = productRepository.findBySku(product2.getSku()).orElseThrow();

        assertThat(find).isEqualTo(product2);
    }

    @Test
    public void shouldAddItems() {
        Product product = em.persist(new Product("T-SHIRT", new BigDecimal("3.45")));

        Item item1 = ItemFixture.builder()
                .product(product)
                .login("one")
                .quantity(3)
                .build(em);

        Item item2 = ItemFixture.builder()
                .product(product)
                .login("two")
                .quantity(5)
                .build(em);

        Product find = productRepository.findById(product.getId()).orElseThrow();

        assertThat(find.getItems())
                .hasSize(2)
                .contains(item1, item2);
    }
}
