package com.fillumina.demo.encryptedid.accounting.service;

import com.fillumina.demo.encryptedid.accounting.domain.Customer;
import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.accounting.repository.CustomerRepository;
import com.fillumina.demo.encryptedid.accounting.repository.InvoiceRepository;
import java.math.BigDecimal;
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
        final UUID customerId = invoiceDTO.getWebUser().getId();
        Customer customer = customerRepository.findByUserId(customerId)
                .orElse(customerRepository.save(new Customer(customerId)));

        BigDecimal total = invoiceDTO.getItems().stream()
                .map(i -> i.getProduct().getPrice().multiply(new BigDecimal(i.getQuantity())))
                .reduce(BigDecimal.ZERO, (a,b) -> a.add(b));

        Invoice invoice = new Invoice(invoiceDTO.getId(), customer, total);

        invoiceRepository.save(invoice);

        return invoice.getId();
    }

    /**
     * Accounting gets an encrypted version of the userId UUID so it's no decrypted here.
     *
     * @param userId
     * @return
     */
    public Customer getCustomer(UUID userId) {
        // using this unneeded variable to help debugging
        Customer customer = customerRepository.findByUserId(userId).orElseThrow();
        return customer;
    }

    public Invoice getInvoice(long invoiceId) {
        return invoiceRepository.getReferenceById(invoiceId);
    }
}
