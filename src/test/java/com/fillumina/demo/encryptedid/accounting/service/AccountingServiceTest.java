package com.fillumina.demo.encryptedid.accounting.service;

import com.fillumina.demo.encryptedid.accounting.domain.Customer;
import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
import com.fillumina.demo.encryptedid.accounting.dto.CustomerDTO;
import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.accounting.dto.ItemDTO;
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
        final UUID customerId = UUID.randomUUID();
        final String customerLogin = "Lollo";
        final String shoppingCartId = "XYZ";

        Customer customer = new Customer(customerId);
        customerRepository.save(customer);

        CustomerDTO customerDTO = new CustomerDTO()
                .id(customerId)
                .login(customerLogin);

        ItemDTO item1 = new ItemDTO("SKU1", "10.99", 2);
        ItemDTO item2 = new ItemDTO("SKU2", "4.33", 3);

        InvoiceDTO invoiceDTO = new InvoiceDTO()
                .id(shoppingCartId)
                .webUser(customerDTO)
                .items(List.of(item1, item2));

        long invoiceId = accountingService.registerNewInvoice(invoiceDTO);

        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow();

        assertThat(invoice.getCustomer()).isEqualTo(customer);
        assertThat(invoice.getShoppingCartId()).isEqualTo(shoppingCartId);
        assertThat(invoice.getTotal()).isEqualTo(
                (new BigDecimal("10.99").multiply(new BigDecimal(2))).add(
                        new BigDecimal("4.33").multiply(new BigDecimal(3))));
    }

    @Test
    public void testGetCustomer() {
        final UUID customerId = UUID.randomUUID();
        final BigDecimal price1 = new BigDecimal("3.45");
        final BigDecimal price2 = new BigDecimal("4.99");

        Customer customer = new Customer(customerId);
        customerRepository.save(customer);

        Invoice invoice1 = new Invoice("ABC", customer, price1);
        Invoice invoice2 = new Invoice("XYZ", customer, price2);

        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        Customer response = accountingService.getCustomer(customerId);

        assertThat(response).isEqualTo(customer);
    }

    @Test
    public void testGetInvoice() {
        final UUID customerId = UUID.randomUUID();
        final BigDecimal price1 = new BigDecimal("3.45");
        final BigDecimal price2 = new BigDecimal("4.99");

        Customer customer = new Customer(customerId);
        customerRepository.save(customer);

        Invoice invoice = new Invoice("ABC", customer, price1);
        invoiceRepository.save(invoice);

        Invoice response = accountingService.getInvoice(invoice.getId());

        assertThat(response).isEqualTo(invoice);
    }
}
