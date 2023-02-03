package com.fillumina.demo.encryptedid.accounting.web;

import com.fillumina.demo.encryptedid.accounting.domain.Customer;
import com.fillumina.demo.encryptedid.accounting.domain.Invoice;
import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.accounting.service.AccountingService;
import com.fillumina.keyencryptor.EncryptorsHolder;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@RestController
public class AccountingController {

    private final AccountingService accountingService;

    public AccountingController(AccountingService accountinService) {
        this.accountingService = accountinService;
    }

    @PostMapping("/accounting/invoices")
    public String registerNewInvoice(@RequestBody InvoiceDTO invoice) {
        long id = accountingService.registerNewInvoice(invoice);
        return "" + id;
    }

    /**
     * Note that even though the {@param customerId} added in the URL it will be
     * encrypted in a TLS (https) connection.
     */
    @GetMapping("/accounting/customers/{customerId}")
    public Customer getCustomer(@PathVariable String customerId) {
        UUID userId = EncryptorsHolder.decryptUuid(customerId);
        Customer customer = accountingService.getCustomer(userId);
        return customer;
    }

    @GetMapping("/accounting/invoices/{invoiceId}")
    public Invoice getInvoice(@PathVariable String invoiceId) {
        Long id = EncryptorsHolder.decryptLongAsUuid(invoiceId);
        Invoice invoice = accountingService.getInvoice(id);
        return invoice;
    }
}
