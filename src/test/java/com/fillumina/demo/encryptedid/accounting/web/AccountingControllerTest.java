package com.fillumina.demo.encryptedid.accounting.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fillumina.demo.encryptedid.accounting.domain.Customer;
import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.accounting.repository.CustomerRepository;
import com.fillumina.demo.encryptedid.accounting.repository.InvoiceRepository;
import com.fillumina.keyencryptor.EncryptorsHolder;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AccountingControllerTest {

    private static final ObjectMapper OM = new ObjectMapper();

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRegisterNewInvoice() throws Exception {
        final UUID customerId = UUID.randomUUID();
        final long shoppingCartId = 123L;
        final BigDecimal price = new BigDecimal("56.78");

        InvoiceDTO invoiceDTO = new InvoiceDTO()
                .customerId(customerId)
                .shoppingCartId(shoppingCartId)
                .total(price);

        mockMvc
            .perform(post("/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OM.writeValueAsBytes(invoiceDTO)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testGetExpenses() throws Exception {
        final UUID customerId = new UUID(0L, 1L);
        final BigDecimal price1 = new BigDecimal("3.45");
        final BigDecimal price2 = new BigDecimal("4.99");

        Customer customer = new Customer(customerId);
        customerRepository.save(customer);

        Invoice invoice1 = new Invoice(1L, customer, price1);
        Invoice invoice2 = new Invoice(2L, customer, price2);

        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        List<BigDecimal> result = List.of(price1, price2);

        String encryptedCustomerId = EncryptorsHolder.encryptUuid(customerId);

        mockMvc
            .perform(get("/invoices/" + encryptedCustomerId))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(OM.writeValueAsString(result)));
    }

}
