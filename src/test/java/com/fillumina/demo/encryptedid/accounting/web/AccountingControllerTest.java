package com.fillumina.demo.encryptedid.accounting.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fillumina.demo.encryptedid.accounting.domain.Customer;
import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
import com.fillumina.demo.encryptedid.accounting.dto.CustomerDTO;
import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.accounting.dto.ItemDTO;
import com.fillumina.demo.encryptedid.accounting.repository.CustomerRepository;
import com.fillumina.demo.encryptedid.accounting.repository.InvoiceRepository;
import com.fillumina.idencryptor.EncryptorsHolder;
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

        mockMvc
            .perform(post("/accounting/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OM.writeValueAsBytes(invoiceDTO)))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void testGetCustomer() throws Exception {
        final UUID customerId = new UUID(0L, 1L);
        final BigDecimal price1 = new BigDecimal("3.45");
        final BigDecimal price2 = new BigDecimal("4.99");

        Customer customer = new Customer(customerId);
        customerRepository.save(customer);

        Invoice invoice1 = new Invoice("XYZ", customer, price1);
        Invoice invoice2 = new Invoice("ABC", customer, price2);

        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);

        String encryptedCustomerId = EncryptorsHolder.encryptUuid(customerId);

        mockMvc
            .perform(get("/accounting/customers/" + encryptedCustomerId))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(OM.writeValueAsString(customer)));
    }

    @Test
    public void testGetInvoice() throws Exception {
        final UUID customerId = new UUID(0L, 1L);
        final BigDecimal price = new BigDecimal("3.45");

        Customer customer = new Customer(customerId);
        customerRepository.save(customer);

        Invoice invoice = new Invoice("XYZ", customer, price);
        invoiceRepository.save(invoice);

        String encryptedInvoiceId =
                EncryptorsHolder.encryptLongAsUuid(Invoice.ENCRYPTED_FIELD_ID ,invoice.getId());

        mockMvc
            .perform(get("/accounting/invoices/" + encryptedInvoiceId))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(OM.writeValueAsString(invoice)));
    }

}
