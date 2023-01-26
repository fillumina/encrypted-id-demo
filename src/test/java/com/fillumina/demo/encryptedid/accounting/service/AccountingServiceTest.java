package com.fillumina.demo.encryptedid.accounting.service;

import com.fillumina.demo.encryptedid.accounting.domain.Customer;
import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.accounting.repository.CustomerRepository;
import com.fillumina.demo.encryptedid.accounting.repository.InvoiceRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@SpringBootTest
@Transactional
public class AccountingServiceTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private AccountingService accountingService;

    @Test
    public void testRegisterNewInvoice() {
        final BigDecimal total = new BigDecimal("34.50");
        final UUID customerId = UUID.randomUUID();
        final long shippingCartId = 1234L;

        Customer customer = new Customer(customerId);
        customerRepository.save(customer);

        InvoiceDTO invoiceDTO = new InvoiceDTO()
                .customerId(customerId)
                .shoppingCartId(shippingCartId)
                .total(total);

        long invoiceId = accountingService.registerNewInvoice(invoiceDTO);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();

        assertThat(invoice.getCustomer()).isEqualTo(customer);
        assertThat(invoice.getShoppingCartId()).isEqualTo(shippingCartId);
        assertThat(invoice.getTotal()).isEqualTo(total);
    }

    @Test
    public void testGetExpensesOfCustomer() {
        final UUID customerId = UUID.randomUUID();
        final BigDecimal price1 = new BigDecimal("3.45");
        final BigDecimal price2 = new BigDecimal("4.99");

        Customer customer = new Customer(customerId);
        customerRepository.save(customer);

        Invoice invoice1 = new Invoice(1L, customer, price1);
        Invoice invoice2 = new Invoice(2L, customer, price2);

        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        List<BigDecimal> response = accountingService.getExpensesOfCustomer(customerId);

        assertThat(response)
                .hasSize(2)
                .containsAll(List.of(price1, price2));
    }
}
