package com.fillumina.demo.encryptedid.shop.repository;

import com.fillumina.demo.encryptedid.shop.domain.ShoppingCart;
import com.fillumina.demo.encryptedid.shop.domain.WebUser;
import jakarta.transaction.Transactional;
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
public class WebUserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private WebUserRepository webUserRepository;

    @Test
    public void shouldHaveTheIdSetAfterCreation() {
        WebUser user = new WebUser("sputnik");

        final UUID userId = user.getId();

        assertThat(userId).isNotNull();
    }

    @Test
    public void shouldFindById() {
        WebUser user = new WebUser("sputnik");

        final UUID userId = user.getId();
        assertThat(userId).isNotNull();

        em.persist(user);

        WebUser finded = webUserRepository.findById(userId).orElseThrow();

        assertThat(finded).isEqualTo(user);
    }

    @Test
    public void shouldAssignManyCartsToAnUser() {
        WebUser user = new WebUser("sputnik");
        em.persist(user);

        ShoppingCart cart1 = new ShoppingCart(user);
        em.persist(cart1);

        ShoppingCart cart2 = new ShoppingCart(user);
        em.persist(cart2);

        ShoppingCart cart3 = new ShoppingCart(user);
        em.persist(cart3);

        WebUser persistedUser = webUserRepository.findById(user.getId()).orElseThrow();
        assertThat(persistedUser.getShoppingCarts())
                .hasSize(3)
                .contains(cart1, cart2, cart3);
    }

}
