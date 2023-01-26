package com.fillumina.demo.encryptedid.shop.client;

import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Implemented with a feign.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@FeignClient(value="accounting", url="${accounting.service.url}")
public interface AccountingClient {

    @PutMapping("/invoices")
    long registerNewInvoice(@RequestBody InvoiceDTO invoice);

    @GetMapping("/invoices/{customerId}")
    List<BigDecimal> getExpenses(@PathVariable String customerId);

}
