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

    public long registerNewInvoice(InvoiceDTO invoiceDTO) {
        Customer customer = customerRepository.findByUserId(invoiceDTO.getCustomerId())
                .orElse(customerRepository.save(new Customer(invoiceDTO.getCustomerId())));

        Invoice invoice = new Invoice(
                invoiceDTO.getShoppingCartId(), customer, invoiceDTO.getTotal());

        invoiceRepository.save(invoice);

        return invoice.getId();
    }

    public List<BigDecimal> getExpensesOfCustomer(UUID userId) {
        Customer customer = customerRepository.findByUserId(userId).orElseThrow();

        return customer.getInvoices().stream()
                .map(Invoice::getTotal)
                .toList();
    }
}
