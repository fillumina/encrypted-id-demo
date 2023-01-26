package com.fillumina.demo.encryptedid.accounting.repository;

import com.fillumina.demo.encryptedid.accounting.domain.Customer;
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
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void shouldNotAddIdBeforePersist() {
        Customer customer = new Customer(UUID.randomUUID());
        assertThat(customer.getId()).isNull();
    }

    @Test
    public void shouldAddIdAfterPersist() {
        Customer customer = new Customer(UUID.randomUUID());
        em.persist(customer);
        assertThat(customer.getId()).isNotNull();
    }

    @Test
    public void shouldFindById() {
        Customer customer = em.persist(new Customer(UUID.randomUUID()));

        Customer find = customerRepository.findById(customer.getId()).orElseThrow();

        assertThat(find).isEqualTo(customer);
    }

    @Test
    public void shouldFindByUserId() {
        final UUID uuid1 = UUID.randomUUID();
        Customer customer1 = em.persist(new Customer(uuid1));

        final UUID uuid2 = UUID.randomUUID();
        Customer customer2 = em.persist(new Customer(uuid2));

        Customer find = customerRepository.findByUserId(uuid2).orElseThrow();

        assertThat(find).isEqualTo(customer2);
    }
}
