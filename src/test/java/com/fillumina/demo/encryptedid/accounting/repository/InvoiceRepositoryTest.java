package com.fillumina.demo.encryptedid.accounting.repository;

import com.fillumina.demo.encryptedid.accounting.domain.Customer;
import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
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
public class InvoiceRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void shouldNotAddIdBeforePersist() {
        Customer customer = new Customer(UUID.randomUUID());
        Invoice invoice = new Invoice(123L, customer, BigDecimal.TEN);
        assertThat(invoice.getId()).isNull();
    }

    @Test
    public void shouldAddIdAfterPersist() {
        Customer customer = new Customer(UUID.randomUUID());
        Invoice invoice = new Invoice(123L, customer, BigDecimal.TEN);

        em.persist(customer);
        em.persist(invoice);

        assertThat(invoice.getId()).isNotNull();
    }

    @Test
    public void shouldFindById() {
        Customer customer = new Customer(UUID.randomUUID());
        Invoice invoice = new Invoice(123L, customer, BigDecimal.TEN);

        em.persist(customer);
        em.persist(invoice);

        Invoice find = invoiceRepository.findById(invoice.getId()).orElseThrow();

        assertThat(find).isEqualTo(invoice);
    }
}
