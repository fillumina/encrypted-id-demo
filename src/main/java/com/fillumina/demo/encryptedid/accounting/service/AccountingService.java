package com.fillumina.demo.encryptedid.accounting.service;

import com.fillumina.demo.encryptedid.accounting.domain.Customer;
import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.accounting.repository.CustomerRepository;
import com.fillumina.demo.encryptedid.accounting.repository.InvoiceRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@Service
public class AccountingService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;

    public AccountingService(CustomerRepository customerRepository,
            InvoiceRepository invoiceRepository) {
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * It's called by the shop API and not directly by the User so the WebUser id is
     * not encrypted
     */
    public long registerNewInvoice(InvoiceDTO invoiceDTO) {
        final UUID customerId = invoiceDTO.getCustomerId();
        Customer customer = customerRepository.findByUserId(invoiceDTO.getCustomerId())
                .orElse(customerRepository.save(new Customer(customerId)));

        Invoice invoice = new Invoice(
                invoiceDTO.getShoppingCartId(), customer, invoiceDTO.getTotal());

        invoiceRepository.save(invoice);

        return invoice.getId();
    }

    /**
     * This might be called by Users and so the UUID is encrypted
     */
    public List<BigDecimal> getExpensesOfCustomer(UUID userId) {
        Customer customer = customerRepository.findByUserId(userId).orElseThrow();

        return customer.getInvoices().stream()
                .map(Invoice::getTotal)
                .toList();
    }
}
