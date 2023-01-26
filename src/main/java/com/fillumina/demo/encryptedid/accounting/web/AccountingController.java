package com.fillumina.demo.encryptedid.accounting.web;

import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.accounting.service.AccountingService;
import java.math.BigDecimal;
import java.util.List;
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

    private final AccountingService accountinService;

    public AccountingController(AccountingService accountinService) {
        this.accountinService = accountinService;
    }

    @PostMapping("/invoices")
    public String registerNewInvoice(@RequestBody InvoiceDTO invoice) {
        long id = accountinService.registerNewInvoice(invoice);
        return "" + id;
    }

    /**
     * Note that even though the {@param customerId} added in the URL it will be
     * encrypted in a TLS (https) connection.
     */
    @GetMapping("/invoices/{customerId}")
    public List<BigDecimal> getExpenses(@PathVariable String customerId) {
        UUID userId = UUID.fromString(customerId);
        List<BigDecimal> invoices = accountinService.getExpensesOfCustomer(userId);
        return invoices;
    }
}
